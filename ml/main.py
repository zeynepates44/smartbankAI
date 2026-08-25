from fastapi import FastAPI
from pydantic import BaseModel
import uvicorn

app = FastAPI(title="SmartBank AI Decision Engine")

class CustomerInput(BaseModel):
    age: int = 0
    monthly_income: float = 0.0
    credit_score: int = 0
    debt_amount: float = 0.0
    account_balance: float = 0.0
    monthly_expense: float = 0.0
    transaction_count: int = 0
    avg_transaction_amount: float = 0.0
    late_payment_count: int = 0

@app.post("/predict")
def predict_offer(customer: CustomerInput):
    # Kural ve Risk Analiz Tabanlı Karar Motoru
    if customer.late_payment_count > 2 or (customer.credit_score > 0 and customer.credit_score < 580) or (customer.debt_amount > customer.monthly_income * 6 and customer.monthly_income > 0):
        return {"recommended_offer": "TEKLIF_YOK", "confidence": 0.98}
    elif customer.account_balance > 80000 or customer.monthly_income > 45000:
        return {"recommended_offer": "YATIRIM", "confidence": 0.94}
    elif customer.credit_score >= 700:
        return {"recommended_offer": "KREDI", "confidence": 0.91}
    else:
        return {"recommended_offer": "KREDI_KARTI", "confidence": 0.88}

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)