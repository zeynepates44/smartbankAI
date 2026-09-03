IF OBJECT_ID('dbo.customers', 'U') IS NOT NULL
BEGIN
    DELETE FROM customers;

    INSERT INTO customers (
        full_name, identity_number, age, occupation,
        monthly_income, monthly_expenses, credit_score,
        account_balance, total_debt, active_credits_count,
        late_payments_count, monthly_transaction_count,
        credit_card_usage_ratio, mobile_app_logins_per_month,
        existing_products
    )
    VALUES
    ('Ahmet Yilmaz', '10254896321', 34, 'Yazilim Muhendisi', 65000.00, 24000.00, 1680, 145000.00, 32000.00, 1, 0, 38, 0.28, 24, 'Vadesiz TL, Kredi Karti'),
    ('Ayse Kaya', '25896314785', 42, 'Mimar', 48000.00, 29000.00, 1340, 21000.00, 85000.00, 2, 1, 14, 0.72, 8, 'Vadesiz TL'),
    ('Mehmet Demir', '36985214796', 28, 'Ogretmen', 32000.00, 22000.00, 1120, 4500.00, 64000.00, 3, 3, 6, 0.94, 2, 'Vadesiz TL, Ek Hesap'),
    ('Zeynep Celik', '45879632145', 31, 'Finans Uzmani', 58000.00, 21000.00, 1720, 230000.00, 12000.00, 0, 0, 45, 0.18, 30, 'Vadesiz TL, Vadeli TL, Kredi Karti'),
    ('Caner Erkin', '74125896325', 49, 'Esnaf', 75000.00, 52000.00, 1410, 68000.00, 110000.00, 2, 0, 22, 0.55, 12, 'Vadesiz Ticari, Kredi Karti');
END