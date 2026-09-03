
from fastapi import FastAPI
from pydantic import BaseModel
from typing import List, Optional
import uvicorn
import joblib
import os
import numpy as np

app = FastAPI(
    title="SmartBank AI Decision Engine",
    description="Multi-Risk (Credit, Fraud, Churn) and Recommendation Support API",
    version="1.0.0"
)

# Istek Modeli
class CustomerAnalysisRequest(BaseModel):
    id: Optional[int] = None
    fullName: Optional[str] = "Bilinmeyen Musteri"
    age: int
    monthlyIncome: float
    monthlyExpenses: float
    creditScore: int
    accountBalance: float
    totalDebt: float
    activeCreditsCount: int = 1
    latePaymentsCount: int = 0
    monthlyTransactionCount: int = 15
    creditCardUsageRatio: float = 0.40
    mobileAppLoginsPerMonth: int = 10
    existingProducts: Optional[str] = "Vadesiz Hesap"

# Risk Metrigi Modeli
class RiskMetric(BaseModel):
    level: str       # LOW, MEDIUM, HIGH
    probability: int # 0 - 100 yuzde
    statusColor: str # success, warning, danger

# Yanit Modeli
class CustomerAnalysisResponse(BaseModel):
    recommendedProduct: str
    recommendationScore: int
    creditRisk: RiskMetric
    fraudRisk: RiskMetric
    churnRisk: RiskMetric
    explanationReasons: List[str]
    aiNaturalLanguageSummary: str

# Egitilmis Random Forest Modelini Yukleme (Varsa)
model = None
MODEL_PATH = "model.joblib"
if os.path.exists(MODEL_PATH):
    try:
        model = joblib.load(MODEL_PATH)
    except Exception:
        model = None

@app.post("/analyze", response_model=CustomerAnalysisResponse)
def analyze_customer(data: CustomerAnalysisRequest):
    reasons = []

    # 1. KREDI RISKI ANALIZI
    # Finansal metrikler: Kredi skoru, borc/gelir orani, gecikmeler
    dti = (data.totalDebt / (data.monthlyIncome * 12)) if data.monthlyIncome > 0 else 1.0
    
    credit_risk_score = 0
    if data.creditScore < 1200:
        credit_risk_score += 45
    elif data.creditScore < 1500:
        credit_risk_score += 25
    else:
        credit_risk_score += 5

    if data.latePaymentsCount > 2:
        credit_risk_score += 35
        reasons.append(f"Gecmis donemde {data.latePaymentsCount} adet gecikmeli odeme kaydi bulundu.")
    elif data.latePaymentsCount > 0:
        credit_risk_score += 15

    if dti > 0.5:
        credit_risk_score += 20
        reasons.append("Borc / Yillik Gelir orani yuksek seviyede.")

    credit_prob = min(max(credit_risk_score, 5), 95)
    if credit_prob >= 60:
        c_level, c_color = "HIGH", "danger"
    elif credit_prob >= 30:
        c_level, c_color = "MEDIUM", "warning"
    else:
        c_level, c_color = "LOW", "success"
        reasons.append("Yuksek kredi skoru ve duzenli geri odeme gecmisi.")

    # 2. FRAUD (DOLANDIRICILIK) RISKI ANALIZI
    # Islem anomalisi, kart limiti asimi, hesap hareketliligi
    fraud_score = 5
    if data.creditCardUsageRatio > 0.90:
        fraud_score += 35
        reasons.append("Kredi karti limit kullanim orani kritik esigin uzerinde (%90+).")
    if data.monthlyTransactionCount > 70:
        fraud_score += 25
    if data.accountBalance < 500 and data.monthlyTransactionCount > 40:
        fraud_score += 20
        reasons.append("Dusuk bakiye ile sira disi siklikta islem hacmi.")

    fraud_prob = min(max(fraud_score, 3), 90)
    if fraud_prob >= 50:
        f_level, f_color = "HIGH", "danger"
    elif fraud_prob >= 25:
        f_level, f_color = "MEDIUM", "warning"
    else:
        f_level, f_color = "LOW", "success"

    # 3. CHURN (MUSTERI TERK) RISKI ANALIZI
    # Mobil giris sayisi, aylik islem adedi, urun sayisi
    churn_score = 10
    if data.mobileAppLoginsPerMonth < 3:
        churn_score += 40
        reasons.append("Mobil bankacilik etkilesimi cok dusuk (Ayda 3'ten az giris).")
    elif data.mobileAppLoginsPerMonth < 8:
        churn_score += 20

    if data.monthlyTransactionCount < 5:
        churn_score += 30
        reasons.append("Hesap hareketliligi ve aylik transfer sayisi inaktif duzeyde.")

    churn_prob = min(max(churn_score, 8), 92)
    if churn_prob >= 55:
        ch_level, ch_color = "HIGH", "danger"
    elif churn_prob >= 30:
        ch_level, ch_color = "MEDIUM", "warning"
    else:
        ch_level, ch_color = "LOW", "success"

    # 4. KISISELLESTIRILMIS URUN ONERISI
    # Gelir duzeyi, bakiye ve risk profiline gore
    if data.monthlyIncome >= 40000 and data.creditScore >= 1400:
        rec_product = "Platinum / Gold Kredi Karti"
        rec_score = 92
        reasons.append("Yuksek gelir ve yuksek kredi guvenilirligi.")
    elif data.accountBalance >= 50000:
        rec_product = "Yuksek Getirili Vadeli Mevduat"
        rec_score = 88
        reasons.append("Vadesiz hesapta atil duran yuksek nakit bakiyesi.")
    elif data.age >= 25 and "Bireysel Emeklilik" not in (data.existingProducts or ""):
        rec_product = "Bireysel Emeklilik Sistemi (BES)"
        rec_score = 79
        reasons.append("Uzun vadeli birikim ve vergi avantaji profiline uygunluk.")
    else:
        rec_product = "Avantajli Ihtiyac Kredisi"
        rec_score = 74

    # 5. DOGAL DIL AI KARAR DESTEK OZETI
    summary = (
        f"Musteri {data.fullName} icin yapilan analizde; Kredi Riski %{credit_prob} ({c_level}), "
        f"Dolandiricilik Riski %{fraud_prob} ({f_level}) ve Bankadan Ayrilma (Churn) Riski %{churn_prob} ({ch_level}) olarak tespit edilmistir. "
        f"Musterinin finansal gostergeleri dogrultusunda en uygun teklif %{rec_score} uygunluk skoru ile '{rec_product}' urunudur."
    )

    return CustomerAnalysisResponse(
        recommendedProduct=rec_product,
        recommendationScore=rec_score,
        creditRisk=RiskMetric(level=c_level, probability=credit_prob, statusColor=c_color),
        fraudRisk=RiskMetric(level=f_level, probability=fraud_prob, statusColor=f_color),
        churnRisk=RiskMetric(level=ch_level, probability=churn_prob, statusColor=ch_color),
        explanationReasons=list(set(reasons)),
        aiNaturalLanguageSummary=summary
    )

@app.get("/health")
def health_check():
    return {"status": "UP", "engine": "SmartBank AI Multi-Risk Engine"}

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
