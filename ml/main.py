from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import joblib
import pandas as pd
import numpy as np
import os

app = FastAPI(title="SmartBank AI Decision Engine")

# Eğitilmiş modeli yükle
MODEL_PATH = "model.joblib"
model = None

if os.path.exists(MODEL_PATH):
    model = joblib.load(MODEL_PATH)
    print("[+] Canlı model.joblib başarıyla yüklendi.")
else:
    print("[!] UYARI: model.joblib bulunamadı! Fallback modu devrede.")

class CustomerFeatures(BaseModel):
    age: int
    monthly_income: float
    credit_score: int
    debt_amount: float
    account_balance: float
    monthly_expense: float
    transaction_count: int
    avg_transaction_amount: float
    late_payment_count: int

@app.get("/")
def read_root():
    return {"status": "online", "service": "SmartBank AI FastAPI Microservice"}

@app.post("/predict")
def predict_offer(features: CustomerFeatures):
    try:
        # Veriyi DataFrame formatına getir
        input_data = pd.DataFrame([{
            'age': features.age,
            'monthly_income': features.monthly_income,
            'credit_score': features.credit_score,
            'debt_amount': features.debt_amount,
            'account_balance': features.account_balance,
            'monthly_expense': features.monthly_expense,
            'transaction_count': features.transaction_count,
            'avg_transaction_amount': features.avg_transaction_amount,
            'late_payment_count': features.late_payment_count
        }])

        if model is not None:
            # Model üzerinden tahmin ve olasılık hesaplama
            prediction = model.predict(input_data)[0]
            probabilities = model.predict_proba(input_data)[0]
            confidence = float(np.max(probabilities))
        else:
            # Yedek kural motoru (Fallback)
            if features.late_payment_count > 2 or features.credit_score < 550:
                prediction = "TEKLIF_YOK"
                confidence = 0.95
            elif features.credit_score >= 750 and features.account_balance > 100000:
                prediction = "YATIRIM"
                confidence = 0.88
            elif features.credit_score >= 650 and features.monthly_income > 30000:
                prediction = "KREDI"
                confidence = 0.85
            else:
                prediction = "KREDI_KARTI"
                confidence = 0.80

        return {
            "recommended_offer": prediction,
            "confidence": round(confidence, 2),
            "customer_id": None
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
