# SmartBank AI

**Yapay Zeka Destekli Bankacılık Müşteri Analiz ve Kişiselleştirilmiş Teklif Karar Destek Sistemi**

SmartBank AI, banka çalışanlarının müşteri verilerini daha hızlı, tutarlı ve anlamlı şekilde değerlendirmesine yardımcı olmak amacıyla geliştirilmiş **uçtan uca web tabanlı bir karar destek sistemi prototipidir**.

Sistem; müşterinin finansal durumu, kredi geçmişi, işlem davranışları ve mevcut bankacılık ürün kullanımını analiz ederek **kredi riski, dolandırıcılık (fraud) riski ve müşteri kaybı (churn) riski** hakkında tahminler üretir. Bunun yanında müşteriye en uygun bankacılık ürününü skorlayarak, banka çalışanının **doğru müşteriye doğru teklifi sunmasını** sağlar.

SmartBank AI'ın temel amacı yalnızca bir risk yüzdesi üretmek değil; farklı veri kaynaklarını bir araya getirerek **kişiselleştirilmiş, gerekçelendirilmiş (XAI) ve çift dilli (TR/EN)** teklif önerileri oluşturmaktır.

---

## Projenin Amacı

Geleneksel bankacılık süreçlerinde müşteri portföylerinin manuel olarak incelenmesi zaman alıcıdır ve gözden kaçabilecek risk faktörleri barındırır.

SmartBank AI bu süreci dijitalleştirmek ve optimize etmek amacıyla:

* 5.000 müşterilik portföy verisini arayüzü kilitlemeden, kutu içi akıcı kaydırma (infinite scroll) ile listeler.
* Anlık arama motoru ile isim, meslek veya TC kimlik numarasına göre gecikmesiz filtreleme sağlar.
* Müşteri finansal profil metriklerini tek ekranda toplar.
* Kredi riskini makine öğrenmesi algoritmalarıyla hesaplar.
* Olağandışı işlem hareketlerine bağlı fraud riskini değerlendirir.
* Müşterinin bankadan ayrılma ihtimalini (churn) tahmin eder.
* Müşteriye en yüksek uyum sağlayan ürünü tekil bir model uygunluk skoruyla sunar.
* Açıklanabilir Yapay Zeka (XAI) yaklaşımıyla teklifin arkasındaki temel gerekçeleri maddeler halinde listeler.
* Karar çıktısını ve risk düzeylerini banka çalışanına hitap eden doğal dil karar özeti (NLG) halinde iletir.
* Bütün arayüzü, dinamik veritabanı kayıtlarını ve model gerekçelerini **Türkçe ve İngilizce** olarak gerçek zamanlı çevirir.

Sistem, banka çalışanının yerine nihai karar veren bir mekanizma değil; **çalışanın karar verme sürecini veri ve yapay zeka ile destekleyen akıllı bir kokpit** olarak tasarlanmıştır.

---

## Temel Özellikler

### 👤 Müşteri Portföyü ve Finansal Profil

Banka çalışanı, 5.000 müşteriyi sayfa düzenini bozmayan sınırlı bir kutu içinde yukarıdan aşağıya doğru kaydırarak inceleyebilir. Listeden bir müşteri seçildiğinde şu veriler anında profil kartına gelir:

* Müşteri ID, TC Kimlik Numarası
* Ad Soyad, Yaş, Meslek
* Aylık Gelir ve Aylık Gider
* Findeks Kredi Skoru
* Hesap Bakiyesi ve Toplam Borç
* Aktif Kredi Sayısı ve Gecikmeli Ödeme Sayısı
* Aylık İşlem Hacmi / Sayısı
* Mevcut Bankacılık Ürünleri (Vadesiz TL, Ek Hesap vb.)

---

### 📊 3'lü Risk Analizi ve Dinamik Yön Rozetleri

Makine öğrenmesi modelleri müşterinin finansal göstergelerini değerlendirerek üç temel risk başlığında olasılık ve seviye belirler. Seviyeler Türkçe ve İngilizce dillerine göre yön oklarıyla gösterilir:

* **Kredi Riski (Credit Risk):** Geri ödeme performansı ve borç/gelir dengesi tahmini.
  * Örnek: `%80` $\rightarrow$ `▲ YÜKSEK` (TR) / `▲ HIGH` (EN)
* **Fraud Riski (Fraud Risk):** Hesap anormallikleri ve olağandışı işlem riski.
  * Örnek: `%5` $\rightarrow$ `▼ DÜŞÜK` (TR) / `▼ LOW` (EN)
* **Churn Riski (Churn Risk):** Müşterinin bankadan ayrılma veya hesabı kapatma riski.
  * Örnek: `%10` $\rightarrow$ `▼ DÜŞÜK` (TR) / `▼ LOW` (EN)

---

### 🎯 Kişiselleştirilmiş Teklif Vitrini

Sistem, müşterinin mevcut ürünlerini ve finansal kapasitesini değerlendirerek en yüksek uyum skoruna sahip ürünü belirler.

Örnek çıktı:

```text
Kişiselleştirilmiş Ürün Teklifi: Platinum / Gold Kredi Kartı
Model Uygunluk Skoru: %92

Açıklanabilir Yapay Zeka (Explainable AI - XAI)
Skorun arkasındaki nedenler banka çalışanının anlayabileceği şeffaf gerekçelerle sunulur.

Örnek çıktı:
Neden Bu Teklif Önerildi? (Açıklanabilirlik)

✔ Yüksek gelir ve yüksek kredi güvenilirliği.
✔ Kredi kartı limit kullanım oranı kritik eşiğin üzerinde (%90+).
✔ Yüksek kredi skoru ve düzenli geri ödeme geçmişi.

Doğal Dil Karar Özeti (NLG Layer)
Analiz sonuçları şablon karmaşasından uzak, seçilen dile göre otomatik olarak oluşturulan akıcı bir karar ve rapor metnine dönüştürülür.

Örnek çıktılar:

Türkçe Özet:

Yapay Zeka Karar Özeti:
Müşteri Ali Çelik için yapılan analizde; Kredi Riski %5 (DÜŞÜK), Dolandırıcılık Riski %5 (DÜŞÜK) ve Bankadan Ayrılma (Churn) Riski %10 (DÜŞÜK) olarak tespit edilmiştir. Müşterinin finansal göstergeleri doğrultusunda en uygun teklif %92 uygunluk skoru ile 'Platinum / Gold Kredi Kartı' ürünüdür.

İngilizce Özet:

AI Decision Summary:
In the analysis conducted for customer Ali Celik; Credit Risk was identified as %5 (LOW), Fraud Risk as %5 (LOW), and Churn Risk as %10 (LOW). In accordance with the customer's financial indicators, the most suitable offer is 'Platinum / Gold Credit Card' with a fit score of %92.


Tam Kapsamlı Dil Desteği (TR / EN)
Navbar üzerindeki dil butonu tıklandığında yalnızca statik etiketler değil; tüm dinamik backend verileri de eş zamanlı olarak tercüme edilir.

Örnek çeviri eşleşmeleri:

Meslekler:        Mimar ➔ Architect | Doktor ➔ Doctor | Bankacı ➔ Banker
Mevcut Ürünler:   Vadesiz TL, Ek Hesap ➔ Demand Deposit Account, Overdraft Account
Teklif Başlığı:   Platinum / Gold Kredi Kartı ➔ Platinum / Gold Credit Card
Risk Rozetleri:   ▲ YÜKSEK ➔ ▲ HIGH | ▼ DÜŞÜK ➔ ▼ LOW

Personel Giriş Portalı
Banka çalışanının operasyonel rolünü seçebileceği açılır (modal) yetkilendirme arayüzü:

Personel ID / Sicil No: (Örn: SB-9042)

Şifre: (Maskelenmiş giriş)

Departman / Rol:

Kredi Tahsis Uzmanı (Credit Underwriter)

Risk Analisti (Risk Analyst)

Şube Müdürü (Branch Manager)

Müşteri Temsilcisi (Customer Representative)

Sistem Mimarisi
SmartBank AI çok katmanlı, mikroservis odaklı ve gevşek bağlı (loosely coupled) bir mimari kullanır:

BANKA ÇALIŞANI
                          │
                          ▼
              ┌────────────────────────┐
              │      WEB ARAYÜZÜ       │
              │   HTML5 / CSS3 / JS    │
              │  Bootstrap 5.3 + Icons │
              │  (TR / EN Dil Motoru)  │
              └───────────┬────────────┘
                          │
                          │ HTTP REST API (JSON)
                          ▼
              ┌────────────────────────┐
              │   SPRING BOOT BACKEND  │
              │       (Port 8081)      │
              │   Spring Data JPA      │
              └───────┬────────┬───────┘
                      │        │
         JDBC Bağlantısı       │ HTTP POST (Müşteri DTO)
                      │        │
                      ▼        ▼
              ┌──────────┐   ┌────────────────────────┐
              │  MSSQL   │   │     PYTHON FASTAPI     │
              │ DATABASE │   │       AI ENGINE        │
              │ (5.000   │   │      (Port 8000)       │
              │ Müşteri) │   └───────────┬────────────┘
              └──────────┘               │
                                         ▼
                             ┌────────────────────────┐
                             │    Scikit-Learn ML     │
                             │ ────────────────────── │
                             │ • Kredi Risk Modeli    │
                             │ • Fraud Risk Modeli    │
                             │ • Churn Risk Modeli    │
                             │ • Ürün Öneri Motoru    │
                             │ • XAI Çıkarım Katmanı  │
                             │ • Dinamik NLG Özeti    │
                             └────────────────────────┘



Teknoloji Stack

Backend
Java 21
Spring Boot (Spring Web, Spring Data JPA)
HikariCP Connection Pool
Maven

Yapay Zeka & Karar Motoru (AI Engine)

Python
FastAPI
Uvicorn (Asenkron ASGI Web Sunucusu)
Scikit-Learn (Random Forest & Karar Kuralları)
NumPy & Pandas

Frontend

HTML5
CSS3 (FinTech tasarımı, kutu içi scrollbar ve responsive grid)
Vanilla JavaScript (DOM manipülasyonu, anlık arama, infinite scroll ve dil haritalama)
Bootstrap 5.3 & Bootstrap Icons

Veritabanı

Microsoft SQL Server (MSSQL)
SQL Server Management Studio (SSMS)
Versiyon Kontrolü
Git & GitHub


Veri Seti

Bu proje geliştirme ve test aşamasında gerçek banka müşteri verileri kullanmamaktadır.
Veritabanında yer alan 5.000 kayıt, bankacılık regülasyonları ve veri gizliliği (KVKK / GDPR) standartlarına uygun olarak üretilmiş sentetik müşteri veri setidir. Veriler; yaş, gelir, kredi skoru, harcama alışkanlıkları ve temerrüt ihtimalleri gibi gerçek dünya korelasyonlarını simüle eder.

Örnek sentetik kayıt yapısı:

Müşteri ID: 266
Ad Soyad: Kaan Özkan
Yaş: 38
Meslek: Mimar
Aylık Gelir: 84.500,00 TL
Aylık Gider: 32.100,00 TL
Kredi Skoru: 1720
Hesap Bakiyesi: 142.300,00 TL
Toplam Borç: 18.400,00 TL
Aktif Krediler: 1
Gecikmeli Ödeme: 0
Mevcut Ürünler: Vadesiz TL, Kredi Kartı



Kullanım Senaryosu
Banka çalışanı sisteme girerek sol paneldeki müşteri portföyünü görüntüler.
Arama çubuğuna isim, meslek veya TC No yazarak müşteriyi anında filtreler.
Tablodaki Seç (Select) butonuna tıklar; müşterinin tüm finansal profili sol alttaki karta yansır.
Sağ tarafta yer alan "Karar Destek Motoru Hazır" ekranı eşliğinde "AI Analizini Başlat (Risk & Teklif)" butonuna basar.
Hazır ekranı kaybolur; yerine Kredi, Fraud ve Churn risk kartları, en uygun ürün teklifi, uygunluk skoru, karar gerekçeleri ve doğal dil özeti gelir.
İstenildiği takdirde sağ üstten EN / TR butonuna basılarak tüm ekran dili ve dinamik gerekçeler çevrilir.


Projenin Sınırları

SmartBank AI bir karar destek sistemi prototipidir.
Gerçek banka müşteri verisi içermez.
Nihai kredi onay/ret yetkisi banka personeline aittir; sistem yalnızca tavsiye niteliğindedir.
Adli veya operasyonel fraud soruşturması yürütmez, sentetik risk olasılıkları sunar.

Projenin Genel Yaklaşımı

Filter → Analyze → Predict → Recommend → Explain
Filter: 5.000 kayıt arasından anında hedef müşteriyi bul.
Analyze: Finansal ve operasyonel verileri incele.
Predict: Kredi, Fraud ve Churn risk olasılıklarını hesapla.
Recommend: En yüksek skora sahip ürünü teklif et.
Explain: Kararın gerekçesini çalışan için şeffaf ve anlaşılır kıl.
