import pandas as pd
import numpy as np
from sqlalchemy import create_engine
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, accuracy_score
import joblib

def load_data_from_mssql():
    """MSSQL SmartBankAI_DB veritabanından müşteri verilerini çeker."""
    connection_string = (
        "mssql+pyodbc://sa:SmartBank123!@localhost:1433/SmartBankAI_DB"
        "?driver=ODBC+Driver+17+for+SQL+Server&TrustServerCertificate=yes"
    )
    try:
        engine = create_engine(connection_string)
        query = "SELECT * FROM customers"
        df = pd.read_sql(query, engine)
        print(f"[*] MSSQL veritabanından {len(df)} adet müşteri kaydı çekildi.")
        return df
    except Exception as e:
        print(f"[!] MSSQL bağlantı hatası: {e}")
        print("[*] Sentetik veri üretiliyor (Fallback modu)...")
        np.random.seed(42)
        n = 5000
        return pd.DataFrame({
            'age': np.random.randint(18, 70, n),
            'monthly_income': np.random.uniform(10000, 150000, n),
            'credit_score': np.random.randint(300, 850, n),
            'debt_amount': np.random.uniform(0, 100000, n),
            'account_balance': np.random.uniform(1000, 500000, n),
            'monthly_expense': np.random.uniform(5000, 80000, n),
            'transaction_count': np.random.randint(1, 150, n),
            'avg_transaction_amount': np.random.uniform(100, 15000, n),
            'late_payment_count': np.random.randint(0, 6, n),
        })

def label_data(df):
    """Bankacılık iş kurallarına göre hedef sınıfları (target) etiketler."""
    conditions = [
        (df['late_payment_count'] > 2) | (df['credit_score'] < 550),
        (df['credit_score'] >= 750) & (df['account_balance'] > 100000),
        (df['credit_score'] >= 650) & (df['monthly_income'] > 30000),
        (df['credit_score'] >= 600)
    ]
    choices = ['TEKLIF_YOK', 'YATIRIM', 'KREDI', 'KREDI_KARTI']
    
    df['recommended_offer'] = np.select(conditions, choices, default='TEKLIF_YOK')
    return df

def train():
    print("=== SmartBank AI Model Eğitimi Başlatılıyor ===")
    
    # 1. Veri Yükleme
    df = load_data_from_mssql()
    
    # Kolon isimlerini küçük harfe normalize et
    df.columns = [col.lower() for col in df.columns]
    
    # Hedef etiket yoksa oluştur
    if 'recommended_offer' not in df.columns:
        df = label_data(df)
        
    features = [
        'age', 'monthly_income', 'credit_score', 'debt_amount',
        'account_balance', 'monthly_expense', 'transaction_count',
        'avg_transaction_amount', 'late_payment_count'
    ]
    
    X = df[features]
    y = df['recommended_offer']
    
    # 2. Train-Test Split
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y
    )
    
    # 3. Model Eğitimi (Random Forest)
    model = RandomForestClassifier(
        n_estimators=100,
        max_depth=12,
        random_state=42,
        class_weight='balanced'
    )
    model.fit(X_train, y_train)
    
    # 4. Değerlendirme
    y_pred = model.predict(X_test)
    acc = accuracy_score(y_test, y_pred)
    
    print(f"\n[+] Model Başarı Oranı (Accuracy): %{acc * 100:.2f}")
    print("\n--- Sınıflandırma Raporu ---")
    print(classification_report(y_test, y_pred))
    
    # 5. Modeli Kaydet
    joblib.dump(model, 'model.joblib')
    print("[+] Model başarıyla 'model.joblib' olarak kaydedildi.")

if __name__ == '__main__':
    train()
