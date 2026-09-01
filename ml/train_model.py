import json
import os
from datetime import datetime
import joblib
import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix, f1_score
from sklearn.model_selection import train_test_split


def generate_synthetic_data(num_samples=5000):
    np.random.seed(42)

    age = np.random.randint(18, 70, size=num_samples)
    monthly_income = np.random.uniform(5000, 150000, size=num_samples)
    monthly_expense = monthly_income * np.random.uniform(0.2, 0.8, size=num_samples)
    debt_amount = np.random.uniform(0, 100000, size=num_samples)
    account_balance = np.random.uniform(500, 500000, size=num_samples)
    credit_score = np.random.randint(300, 850, size=num_samples)
    late_payment_count = np.random.poisson(lam=1.5, size=num_samples)
    transaction_count = np.random.randint(5, 100, size=num_samples)

    offers = []
    for i in range(num_samples):
        if credit_score[i] > 700 and account_balance[i] > 100000:
            offers.append("YATIRIM")
        elif credit_score[i] > 600 and monthly_income[i] > 25000 and debt_amount[i] < monthly_income[i] * 4:
            offers.append("KREDI")
        elif credit_score[i] > 550 and transaction_count[i] > 20 and late_payment_count[i] <= 2:
            offers.append("KREDI_KARTI")
        else:
            offers.append("TEKLIF_YOK")

    df = pd.DataFrame({
        "age": age,
        "monthlyIncome": monthly_income,
        "monthlyExpense": monthly_expense,
        "debtAmount": debt_amount,
        "accountBalance": account_balance,
        "creditScore": credit_score,
        "latePaymentCount": late_payment_count,
        "transactionCount": transaction_count,
        "target": offers
    })

    return df


def train():
    current_dir = os.path.dirname(os.path.abspath(__file__))
    model_path = os.path.join(current_dir, "model.joblib")
    metadata_path = os.path.join(current_dir, "metadata.json")

    print("[1/4] Sentetik veri seti üretiliyor...")
    df = generate_synthetic_data()

    feature_cols = [
        "age", "monthlyIncome", "monthlyExpense", "debtAmount",
        "accountBalance", "creditScore", "latePaymentCount", "transactionCount"
    ]

    X = df[feature_cols]
    y = df["target"]

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y
    )

    print("[2/4] Random Forest modeli eğitiliyor...")
    model_params = {
        "n_estimators": 100,
        "max_depth": 10,
        "random_state": 42
    }
    model = RandomForestClassifier(**model_params)
    model.fit(X_train, y_train)

    print("[3/4] Model metrikleri ve Confusion Matrix hesaplanıyor...")
    y_pred = model.predict(X_test)
    classes = sorted(list(y.unique()))

    acc = float(accuracy_score(y_test, y_pred))
    f1_weighted = float(f1_score(y_test, y_pred, average="weighted"))
    cm = confusion_matrix(y_test, y_pred, labels=classes).tolist()
    report = classification_report(y_test, y_pred, labels=classes, output_dict=True)

    metadata = {
        "model_name": "SmartBank-Customer-Decision-RF",
        "model_version": "1.0.0",
        "trained_at": datetime.utcnow().isoformat() + "Z",
        "algorithm": "RandomForestClassifier",
        "hyperparameters": model_params,
        "features": feature_cols,
        "classes": classes,
        "dataset_summary": {
            "total_samples": len(df),
            "train_samples": len(X_train),
            "test_samples": len(X_test)
        },
        "metrics": {
            "accuracy": round(acc, 4),
            "f1_score_weighted": round(f1_weighted, 4),
            "classification_report": report,
            "confusion_matrix": {
                "labels": classes,
                "matrix": cm
            }
        }
    }

    print("[4/4] Model ve Metadata dosyaları kaydediliyor...")
    joblib.dump(model, model_path)
    with open(metadata_path, "w", encoding="utf-8") as f:
        json.dump(metadata, f, ensure_ascii=False, indent=4)

    print(f"Başarıyla tamamlandı!")
    print(f" -> Model: {model_path}")
    print(f" -> Metadata: {metadata_path}")
    print(f" -> Accuracy: %{round(acc * 100, 2)} | Weighted F1: {round(f1_weighted, 4)}")


if __name__ == "__main__":
    train()
