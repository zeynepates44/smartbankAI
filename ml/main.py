import json
import os
import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field

app = FastAPI(
    title="SmartBank AI Decision Engine",
    description="Akıllı Bankacılık Müşteri Karar ve Ürün Öneri API Servisi",
    version="1.0.0"
)

# CORS Middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Dizin ve Dosya Yolları
CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
MODEL_PATH = os.path.join(CURRENT_DIR, "model.joblib")
METADATA_PATH = os.path.join(CURRENT_DIR, "metadata.json")

# Model Yükleme
model = None
if os.path.exists(MODEL_PATH):
    try:
        model = joblib.load(MODEL_PATH)
        print(f"[+] Model basariyla yuklendi: {MODEL_PATH}")
    except Exception as e:
        print(f"[-] Model yuklenirken hata: {e}")
else:
    print(f"[!] Model dosyasi bulunamadi: {MODEL_PATH}")


# Pydantic İstek ve Yanıt Şemaları
class CustomerFeatures(BaseModel):
    age: int = Field(..., example=35)
    monthlyIncome: float = Field(..., example=45000.0)
    monthlyExpense: float = Field(..., example=15000.0)
    debtAmount: float = Field(..., example=20000.0)
    accountBalance: float = Field(..., example=85000.0)
    creditScore: int = Field(..., example=720)
    latePaymentCount: int = Field(..., example=0)
    transactionCount: int = Field(..., example=42)


class PredictionResponse(BaseModel):
    recommended_offer: str
    confidence: float
    explanation: str
    is_fallback: bool = False


# 1. Kök Dizin
@app.get("/", tags=["Monitoring"])
def root():
    return {
        "message": "SmartBank AI Decision Engine Aktif",
        "docs": "/docs",
        "health": "/health",
        "metadata": "/model-metadata"
    }


# 2. Health Endpoint (Canlılık ve Model Kontrolü)
@app.get("/health", tags=["Monitoring"])
def health_check():
    model_loaded = model is not None
    return {
        "status": "UP" if model_loaded else "DEGRADED",
        "service": "SmartBank AI Decision Engine",
        "model_loaded": model_loaded,
        "framework": "FastAPI",
        "environment": "production"
    }


# 3. Model Metadata & Confusion Matrix Endpoint
@app.get("/model-metadata", tags=["Model Analytics"])
def get_model_metadata():
    if not os.path.exists(METADATA_PATH):
        raise HTTPException(
            status_code=404,
            detail="Model metadatasi bulunamadi. Lutfen train_model.py scriptini calistirin."
        )

    try:
        with open(METADATA_PATH, "r", encoding="utf-8") as f:
            metadata = json.load(f)
        return metadata
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Metadata okunamadi: {str(e)}")


# 4. AI Karar ve Tahmin Uç Noktası
@app.post("/predict", response_model=PredictionResponse, tags=["Prediction"])
def predict(features: CustomerFeatures):
    if model is None:
        raise HTTPException(status_code=503, detail="AI Modeli henuz bellege yuklenmedi.")

    try:
        input_data = pd.DataFrame([{
            "age": features.age,
            "monthlyIncome": features.monthlyIncome,
            "monthlyExpense": features.monthlyExpense,
            "debtAmount": features.debtAmount,
            "accountBalance": features.accountBalance,
            "creditScore": features.creditScore,
            "latePaymentCount": features.latePaymentCount,
            "transactionCount": features.transactionCount
        }])

        prediction = model.predict(input_data)[0]
        probabilities = model.predict_proba(input_data)[0]
        confidence = float(max(probabilities))

        explanations = {
            "YATIRIM": f"Musterinin kredi skoru ({features.creditScore}) ve bakiye durumu yuksek portfoy yatirim teklifini desteklemektedir.",
            "KREDI": f"Musterinin gelir/borc dengesi ve kredi skoru ({features.creditScore}) ihtiyac kredisi tahsisine uygundur.",
            "KREDI_KARTI": f"Islem hacmi ({features.transactionCount}) ve dusuk gecikme gecmisi kart teklifi icin uygundur.",
            "TEKLIF_YOK": "Mevcut finansal gostergeler standart risk esiklerinin altindadir, teklif onerilmemektedir."
        }

        return PredictionResponse(
            recommended_offer=prediction,
            confidence=round(confidence, 2),
            explanation=explanations.get(prediction, "Model tahminine gore teklif uretildi."),
            is_fallback=False
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Tahmin uretilirken hata olustu: {str(e)}")
