import os
import json
import datetime
import pandas as pd
import numpy as np
from sqlalchemy import create_engine
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, accuracy_score, confusion_matrix
import joblib

def load_data_from_mssql():
    """MSSQL SmartBankAI_DB veritabanından müşteri verilerini çeker veya sentetik üretir."""
    db_user = os.getenv("DB_USERNAME", "sa")
    db_pass = os.getenv("DB_PASSWORD", "SmartBank123!")
    db_host = os.getenv("DB_HOST", "localhost")
    db_port = os.getenv("DB_PORT", "1433")
    db_name = os.getenv("DB_NAME", "SmartBankAI_DB")

    connection_string = (
        f"mssql+pyodbc://{db_user}:{db_pass}@{db_host}:{db_port}/{db_name}"
        "?driver=ODBC+Driver+17+for+SQL+Server&TrustServerCertificate=yes"
    )
    try:
        engine = create_engine(connection_string)
        query = "SELECT * FROM customers"
        df = pd.read_sql(query, engine)
        print(f"[*] MSSQL veritabanından {len(df)} adet müşteri kaydı başarıyla çekildi.")
        return df
    except Exception as e:
        print(f"[!] MSSQL bağlantı hatası: {e}")
        print("[*] Sentetik veri üretiliyor (Fallback modu)...")
        np.random.seed(42)
        n = 5000
        return pd.DataFrame({
            'age': np.random.randint(18, 70, n),
            'monthly_income': np.random.uniform(15000, 120000, n),
            'credit_score': np.random.randint(450, 850, n),
            'debt_amount': np.random.uniform(0, 80000, n),
            'account_balance': np.random.uniform(5000, 350000, n),
            'monthly_expense': np.random.uniform(5000, 50000, n),
            'transaction_count': np.random.randint(5, 120, n),
            'avg_transaction_amount': np.random.uniform(200, 8000, n),
            'late_payment_count': np.random.choice([0, 1, 2, 3, 4], size=n, p=[0.55, 0.20, 0.12, 0.08, 0.05]),
        })

def label_data(df):
    """Gerçekçi ve dengeli bankacılık segmentasyon kuralları"""
    conditions = [
        # Yüksek risk -> Teklif Yok
        (df['late_payment_count'] >= 3) | (df['credit_score'] < 520),
        
        # Varlıklı / Yüksek Skor -> Yatırım Ürünleri (Mevduat / Fon)
        (df['credit_score'] >= 720) & (df['account_balance'] >= 60000),
        
        # Düzenli Gelir & Düşük Gecikme -> İhtiyaç/Taşıt Kredisi
        (df['credit_score'] >= 620) & (df['monthly_income'] >= 35000) & (df['late_payment_count'] <= 1),
        
        # Aktif Harcama / Standart Skor -> Kredi Kartı Limit Artışı / Yeni Kart
        (df['credit_score'] >= 550) & (df['transaction_count'] >= 15)
    ]
    choices = ['TEKLIF_YOK', 'YATIRIM', 'KREDI', 'KREDI_KARTI']
    df['recommended_offer'] = np.select(conditions, choices, default='KREDI_KARTI')
    return df

def train():
    print("=== SmartBank AI Model Eğitimi Başlatılıyor ===")
    
    df = load_data_from_mssql()
    df.columns = [col.lower() for col in df.columns]
    
    # Hedef sınıf etiketleme
    df = label_data(df)
    
    features = [
        'age', 'monthly_income', 'credit_score', 'debt_amount',
        'account_balance', 'monthly_expense', 'transaction_count',
        'avg_transaction_amount', 'late_payment_count'
    ]
    
    X = df[features]
    y = df['recommended_offer']
    
    print("\n--- Hedef Sınıf Dağılımı ---")
    print(y.value_counts())
    
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y
    )
    
    model = RandomForestClassifier(
        n_estimators=100,
        max_depth=12,
        random_state=42,
        class_weight='balanced'
    )
    model.fit(X_train, y_train)
    
    y_pred = model.predict(X_test)
    acc = accuracy_score(y_test, y_pred)
    cm = confusion_matrix(y_test, y_pred).tolist()
    report = classification_report(y_test, y_pred, output_dict=True)
    
    print(f"\n[+] Model Doğruluk Oranı (Accuracy): %{acc * 100:.2f}")
    
    # 1. Eğitilmiş Modeli Kaydet
    joblib.dump(model, 'model.joblib')
    print("[+] 'model.joblib' başarıyla oluşturuldu ve kaydedildi.")
    
    # 2. Model Metadata JSON Dosyasını Kaydet
    metadata = {
        "model_name": "RandomForestClassifier",
        "accuracy": round(acc, 4),
        "classes": list(model.classes_),
        "feature_names": features,
        "parameters": model.get_params(),
        "confusion_matrix": cm,
        "classification_report": report,
        "trained_at": datetime.datetime.now().isoformat()
    }
    
    with open('model_metadata.json', 'w', encoding='utf-8') as f:
        json.dump(metadata, f, indent=4, ensure_ascii=False)
    print("[+] Model metrikleri ve rapor 'model_metadata.json' dosyasına kaydedildi.")

if __name__ == '__main__':
    train()
