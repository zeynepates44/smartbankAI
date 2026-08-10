# SmartBank AI

**Yapay Zeka Destekli Bankacılık Müşteri Analiz ve Kişiselleştirilmiş Teklif Karar Destek Sistemi**

SmartBank AI, banka çalışanlarının müşteri verilerini daha hızlı ve anlamlı şekilde analiz etmesine yardımcı olmak amacıyla geliştirilen **web tabanlı bir karar destek sistemi prototipidir**.

Sistem; müşterinin finansal durumu, kredi geçmişi, işlem davranışları ve bankacılık ürün kullanımını analiz ederek **kredi riski, dolandırıcılık (fraud) riski ve müşteri kaybı (churn) riski** hakkında tahminler üretir. Bunun yanında müşteriye uygun olabilecek bankacılık ürünlerini sıralayarak, banka çalışanının **doğru müşteriye doğru ürünü sunmasına** yardımcı olur.

SmartBank AI'ın temel amacı yalnızca bir risk skoru üretmek değil; farklı müşteri verilerini bir araya getirerek **kişiselleştirilmiş ve açıklanabilir teklif önerileri** oluşturmaktır.

---

## Projenin Amacı

Geleneksel bankacılık sistemlerinde müşterilerle ilgili çok sayıda veri bulunmasına rağmen bu verilerin tamamının bir banka çalışanı tarafından manuel olarak değerlendirilmesi zaman alıcı olabilir.

SmartBank AI bu süreci desteklemek amacıyla:

* Müşteri profilini analiz eder.
* Kredi riskini tahmin eder.
* Olağandışı işlem davranışlarına bağlı fraud riskini analiz eder.
* Müşterinin bankadan ayrılma ihtimalini tahmin eder.
* Müşteriye uygun bankacılık ürünlerini sıralar.
* Ürün önerilerinin hangi müşteri özelliklerine dayandığını açıklar.
* Lokal bir LLM kullanarak model sonuçlarını banka çalışanının daha kolay anlayabileceği doğal bir dille açıklar.

Bu nedenle sistem, banka çalışanının yerine karar veren bir yapı değil, **çalışanın karar verme sürecini destekleyen bir yapay zeka sistemi** olarak tasarlanmıştır.

---

## Temel Özellikler

### 👤 Müşteri Analizi

Banka çalışanı sistem üzerinden bir müşteriyi seçerek müşterinin temel finansal ve davranışsal bilgilerini görüntüleyebilir.

Analiz kapsamında örneğin:

* Yaş
* Meslek
* Aylık gelir
* Kredi skoru
* Hesap bakiyesi
* Toplam borç
* Aktif kredi sayısı
* Gecikmeli ödeme sayısı
* İşlem sıklığı
* Kart kullanım davranışı
* Mobil bankacılık kullanım sıklığı
* Mevcut bankacılık ürünleri

gibi bilgiler değerlendirilebilir.

---

### 📊 Kredi Risk Analizi

Makine öğrenmesi modeli, müşterinin finansal geçmişini ve kredi davranışlarını analiz ederek kredi geri ödeme riski hakkında tahmin üretir.

Örnek çıktı:

```text
Credit Risk
Low

Risk Probability
12%
```

Bu sonuç gerçek bir kredi onayı veya reddi değildir. Sistem yalnızca banka çalışanına yönelik **karar destek çıktısı** üretir.

---

### 🛡️ Fraud Risk Analizi

Müşterinin işlem davranışları incelenerek olağandışı işlem davranışlarına bağlı risk tahmini yapılır.

Model tarafından değerlendirilebilecek özellikler arasında:

* İşlem tutarı
* İşlem sıklığı
* İşlem zamanı
* İşlem lokasyonu
* Ortalama işlem tutarından sapma
* Kısa sürede gerçekleştirilen işlem sayısı

gibi değişkenler bulunabilir.

Örnek:

```text
Fraud Risk
Low

Risk Probability
5%
```

Bu modül gerçek bir bankacılık fraud tespit sistemi değildir; sentetik veri üzerinde geliştirilen **prototip niteliğinde bir karar destek modülüdür**.

---

### 📉 Churn Risk Analizi

Sistem, müşterinin bankacılık hizmetlerini kullanma davranışlarını analiz ederek müşterinin bankadan ayrılma ihtimali hakkında tahmin üretir.

Değerlendirilebilecek özellikler:

* Son işlem tarihi
* İşlem sıklığı
* Mobil bankacılık kullanım sıklığı
* Hesap bakiyesi
* Ürün kullanım sayısı
* Kart kullanım oranı

Örnek:

```text
Churn Risk
Medium

Risk Probability
34%
```

---

## 🎯 Kişiselleştirilmiş Ürün Önerisi

SmartBank AI'ın temel farklılaştırıcı özelliklerinden biri **müşteriye özel ürün önerisi** oluşturmasıdır.

Sistem müşterinin finansal ve davranışsal profilini analiz ederek uygun olabilecek ürünleri sıralar.

Örneğin:

```text
Recommended Products

1. Gold Credit Card       91%
2. Private Pension        77%
3. Time Deposit           63%
```

Buradaki skor, müşterinin ürünü kesin olarak satın alacağı anlamına gelmez. Skor, modelin mevcut müşteri özelliklerine göre hesapladığı **ürün uygunluk tahminini** ifade eder.

Amaç:

> **Doğru müşteriye, doğru bankacılık ürününü önermek.**

---

## 💡 Açıklanabilir Yapay Zeka

SmartBank AI yalnızca bir skor üretmek yerine, mümkün olduğunca bu skorun arkasındaki müşteri özelliklerini de göstermeyi amaçlar.

Örneğin:

```text
Gold Credit Card
Recommendation Score: 91%

Why?

- High credit score
- Regular payment history
- High card usage
- Suitable income level
```

Bu yaklaşım sayesinde banka çalışanı yalnızca bir yüzde görmek yerine önerinin hangi müşteri özellikleriyle ilişkili olduğunu anlayabilir.

---

## 🧠 Lokal LLM Kullanımı

Projede dış bir yapay zeka API'sine bağımlı kalmak yerine **lokal bir Large Language Model (LLM)** kullanılmaktadır.

LLM, sistemde karar verici olarak kullanılmaz.

Makine öğrenmesi modelleri:

* Kredi riskini
* Fraud riskini
* Churn riskini
* Ürün uygunluk skorlarını

hesaplar.

Lokal LLM ise bu sonuçları ve seçilmiş müşteri özelliklerini kullanarak banka çalışanına yönelik **doğal dilde açıklamalar** üretir.

Örneğin:

```text
Gold Credit Card – 91%

Müşterinin yüksek kredi skoru, düzenli ödeme
geçmişi ve mevcut kart kullanım davranışı
Gold Credit Card için yüksek uygunluk
göstermektedir.
```

### LLM'in sınırları

LLM:

* Kredi kararı vermez.
* Fraud kararı vermez.
* Müşterinin bankadan ayrılıp ayrılmayacağına tek başına karar vermez.
* Ürün uygunluk skorunu tek başına hesaplamaz.
* Veritabanına doğrudan erişmez.
* Otonom bir AI agent olarak çalışmaz.

Bu mimaride LLM, **açıklama ve yorumlama katmanı** olarak kullanılmaktadır.

---

# Sistem Mimarisi

SmartBank AI genel olarak aşağıdaki mimariyi kullanır:

```text
                    BANKA ÇALIŞANI
                          │
                          ▼
                ┌──────────────────┐
                │   WEB ARAYÜZÜ    │
                │ HTML / CSS / JS  │
                │    Bootstrap     │
                └────────┬─────────┘
                         │
                         ▼
                ┌──────────────────┐
                │   SPRING BOOT    │
                │     BACKEND      │
                └───────┬───┬──────┘
                        │   │
              ┌─────────┘   └──────────┐
              ▼                        ▼
      ┌───────────────┐       ┌────────────────┐
      │    MSSQL      │       │ ML MODELLERİ   │
      │   DATABASE    │       │                │
      └───────────────┘       │ Credit Risk    │
                              │ Fraud Risk     │
                              │ Churn Risk     │
                              │ Recommendation │
                              └───────┬────────┘
                                      │
                                      ▼
                              ┌───────────────┐
                              │ LOCAL LLM     │
                              │   (Ollama)    │
                              └───────┬───────┘
                                      │
                                      ▼
                               AI AÇIKLAMASI
```

---

# Teknoloji Stack

## Backend

* **Java**
* **Spring Boot**
* **Spring Data JPA**
* **Maven**

Spring Boot, uygulamanın backend ve REST API katmanını oluşturmak için kullanılmaktadır.

---

## Frontend

* **HTML**
* **CSS**
* **JavaScript**
* **Bootstrap**
* **Chart.js**

Frontend, banka çalışanlarının müşterileri araması, müşteri profillerini görüntülemesi ve yapay zeka analiz sonuçlarını incelemesi için kullanılmaktadır.

---

## Database

* **Microsoft SQL Server**
* **SQL Server Management Studio (SSMS)**

Müşteri, kredi geçmişi, işlem ve ürün bilgilerinin saklanması için ilişkisel veritabanı kullanılmaktadır.

---

## Machine Learning

Projede müşteri verilerinden tahminler üretmek amacıyla Java tabanlı makine öğrenmesi yaklaşımı kullanılmaktadır.

Modeller:

```text
Credit Risk Model
Fraud Risk Model
Churn Risk Model
Product Recommendation Model
```

---

## Local AI

* **Ollama**
* **Local Large Language Model (LLM)**

Lokal LLM, makine öğrenmesi sonuçlarını doğal dilde açıklamak amacıyla kullanılmaktadır.

---

## Version Control

* **Git**
* **GitHub**

Proje geliştirme sürecinin takip edilmesi ve kod versiyonlarının yönetilmesi için kullanılmaktadır.

---

# Veri Seti

Bu proje geliştirme ve test aşamasında **gerçek banka müşteri verileri kullanmamaktadır**.

Bunun yerine sentetik müşteri verileri kullanılmaktadır.

Sentetik veri; gerçek kişilere ait olmayan ancak gerçek dünyadaki müşteri davranışlarını belirli ölçüde simüle edecek şekilde oluşturulan yapay verilerdir.

Örneğin:

```text
Customer ID: 10254
Age: 34
Monthly Income: 52,000
Credit Score: 1540
Total Debt: 78,000
Late Payments: 0
Account Balance: 125,000
```

Bu bilgiler gerçek bir müşteriye ait değildir.

Prototipin amacı gerçek banka verilerine erişmek değil, yapay zeka tabanlı karar destek sisteminin çalışma mantığını göstermektir.

---

# Projenin Farklılaştırıcı Noktası

Bankacılık uygulamalarında müşteri analizi, risk değerlendirmesi ve ürün önerisi gibi özellikler ayrı sistemlerde bulunabilir.

SmartBank AI'ın temel yaklaşımı bu analizleri tek bir karar destek akışında birleştirmektir:

```text
Müşteri Verisi
      ↓
Risk Analizi
      ↓
Davranış Analizi
      ↓
Ürün Uygunluğu
      ↓
Kişiselleştirilmiş Teklif
      ↓
Açıklanabilir AI Sonucu
```

Bu sayede sistem yalnızca:

> "Bu müşterinin riski nedir?"

sorusuna değil,

> **"Bu müşteriye hangi ürün daha uygun olabilir ve bunu neden öneriyoruz?"**

sorusuna da cevap vermeyi amaçlamaktadır.

---

# Kullanım Senaryosu

Örnek bir banka çalışanı senaryosu:

1. Banka çalışanı sisteme giriş yapar.
2. Müşteri numarasını arar.
3. Sistem müşterinin finansal profilini getirir.
4. Müşteri analizini başlatır.
5. Kredi riski hesaplanır.
6. Fraud riski değerlendirilir.
7. Churn riski tahmin edilir.
8. Müşteriye uygun ürünler sıralanır.
9. Sistem önerinin temel nedenlerini gösterir.
10. Lokal LLM, sonuçları doğal dilde özetler.

Örneğin:

```text
Customer: 10254

Credit Risk: LOW – 12%
Fraud Risk: LOW – 5%
Churn Risk: MEDIUM – 34%

Recommended Product:
Gold Credit Card – 91%

Explanation:
Müşterinin yüksek kredi skoru, düzenli ödeme
geçmişi ve mevcut kart kullanım davranışı
Gold Credit Card için yüksek uygunluk
göstermektedir.
```

---

# Projenin Sınırları

SmartBank AI bir **staj projesi ve prototip** olarak geliştirilmektedir.

Bu nedenle:

* Gerçek banka müşteri verisi kullanılmamaktadır.
* Gerçek kredi onayı verilmemektedir.
* Gerçek fraud soruşturması yapılmamaktadır.
* Gerçek müşteri işlemleri gerçekleştirilmemektedir.
* Model sonuçları finansal karar olarak değerlendirilmemelidir.
* Sistem gerçek bir bankacılık altyapısının yerine geçmemektedir.
* Lokal LLM nihai karar verici değildir.

Sistem, bankacılık sektöründe yapay zeka destekli karar destek mekanizmalarının nasıl tasarlanabileceğini göstermek amacıyla geliştirilmiştir.

---

# Gelecekte Geliştirilebilecek Özellikler

Projenin daha ileri bir sürümünde:

* Gerçek zamanlı işlem analizi
* Daha gelişmiş fraud detection modelleri
* Gelişmiş recommendation sistemleri
* Model açıklanabilirliği için SHAP/LIME benzeri yöntemler
* Kullanıcı ve rol bazlı yetkilendirme
* Daha gelişmiş müşteri segmentasyonu
* Gerçek zamanlı dashboard
* Model performans takip sistemi
* MLOps altyapısı
* Docker containerization
* Kubernetes tabanlı deployment
* Kurumsal authentication
* Gerçek banka sistemleriyle güvenli API entegrasyonu

gibi özellikler eklenebilir.

---

# Projenin Genel Hedefi

SmartBank AI'ın temel hedefi, banka çalışanlarının müşteri verilerini daha etkin değerlendirmesine yardımcı olarak **veriye dayalı, kişiselleştirilmiş ve açıklanabilir müşteri teklifleri** oluşturabilen bir karar destek sistemi geliştirmektir.

Projenin temel yaklaşımı:

> **Analyze → Predict → Recommend → Explain**

şeklinde özetlenebilir.

**Analyze:** Müşteri verilerini analiz et.

**Predict:** Riskleri ve müşteri davranışlarını tahmin et.

**Recommend:** Müşteriye uygun ürünleri öner.

**Explain:** Önerinin nedenini banka çalışanına açıkla.
