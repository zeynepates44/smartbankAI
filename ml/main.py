import os
import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(
    title="SmartBank AI Decision Engine",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Model yükleme
MODEL_PATH = os.path.join(os.path.dirname(__file__), "model.joblib")
model = None

if os.path.exists(MODEL_PATH):
    model = joblib.load(MODEL_PATH)
    print(f"[+] Model basariyla yuklendi: {MODEL_PATH}")
else:
    print(f"[!] UYARI: Model dosyasi bulunamadi: {MODEL_PATH}")

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
def root():
    return {"message": "SmartBank AI Decision Engine Aktif"}

@app.get("/health")
def health_check():
    return {
        "status": "UP",
        "service": "SmartBank AI Engine",
        "model_loaded": model is not None
    }

@app.post("/predict")
def predict_offer(features: CustomerFeatures):
    if model is None:
        raise HTTPException(status_code=503, detail="Model yuklenemedi.")
    
    feature_order = [
        'age', 'monthly_income', 'credit_score', 'debt_amount',
        'account_balance', 'monthly_expense', 'transaction_count',
        'avg_transaction_amount', 'late_payment_count'
    ]
    
    input_df = pd.DataFrame([[
        features.age,
        features.monthly_income,
        features.credit_score,
        features.debt_amount,
        features.account_balance,
        features.monthly_expense,
        features.transaction_count,
        features.avg_transaction_amount,
        features.late_payment_count
    ]], columns=feature_order)
    
    try:
        prediction = model.predict(input_df)[0]
        probabilities = model.predict_proba(input_df)[0]
        max_confidence = float(max(probabilities))
        
        return {
            "recommended_offer": str(prediction),
            "confidence": round(max_confidence, 4),
            "status": "SUCCESS"
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Tahmin hatasi: {str(e)}")
