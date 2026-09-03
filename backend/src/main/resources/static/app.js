let allCustomers = [];
let filteredCustomers = [];
let selectedCustomerId = null;
let currentLang = "tr";
let renderLimit = 30;

let lastAnalysisData = null;

// --- DİL SÖZLÜĞÜ ---
const dict = {
    tr: {
        portfolio: "Müşteri Portföyü",
        searchPlaceholder: "İsim, meslek veya TC ile ara...",
        colName: "Ad Soyad",
        colJob: "Meslek",
        colAction: "İşlem",
        selectBtn: "Seç",
        profileTitle: "Müşteri Finansal Profili",
        income: "Aylık Gelir:",
        expenses: "Aylık Gider:",
        creditScore: "Kredi Skoru:",
        balance: "Hesap Bakiyesi:",
        debt: "Toplam Borç:",
        credits: "Aktif Krediler:",
        latePay: "Gecikmeli Ödeme:",
        txCount: "Aylık İşlem Sayısı:",
        products: "Mevcut Ürünler:",
        runAiBtn: "AI Analizini Başlat (Risk & Teklif)",
        analyzingBtn: "Analiz Ediliyor...",
        welcomeHeader: "Karar Destek Motoru Hazır",
        welcomeDesc: "Analiz gerçekleştirmek için sol taraftaki listeden bir müşteri seçip analizi başlatın.",
        creditRisk: "KREDİ RİSKİ",
        fraudRisk: "FRAUD RİSKİ",
        churnRisk: "CHURN RİSKİ",
        recProductHeader: "Kişiselleştirilmiş Ürün Teklifi",
        recScoreLabel: "Uygunluk Skoru",
        xaiHeader: "Neden Bu Teklif Önerildi? (Açıklanabilirlik)",
        summaryHeader: "Yapay Zeka Karar Özeti:",
        staffLogin: "Personel Girişi",
        modalTitle: "Personel Giriş Portalı",
        modalDesc: "Sisteme personel kimliği ile devam edebilirsiniz (İsteğe bağlıdır).",
        lblStaffId: "Personel ID / Sicil No",
        lblPassword: "Şifre",
        lblRole: "Departman / Meslek",
        roleCredit: "Kredi Tahsis Uzmanı",
        roleRisk: "Risk Analisti",
        roleManager: "Şube Müdürü",
        roleRep: "Müşteri Temsilcisi",
        btnClose: "Kapat",
        btnLogin: "Giriş Yap",
        noJob: "Meslek Belirtilmedi",
        none: "Yok",
        noResult: "Kayıt bulunamadı."
    },
    en: {
        portfolio: "Customer Portfolio",
        searchPlaceholder: "Search by name, job or ID...",
        colName: "Full Name",
        colJob: "Occupation",
        colAction: "Action",
        selectBtn: "Select",
        profileTitle: "Customer Financial Profile",
        income: "Monthly Income:",
        expenses: "Monthly Expenses:",
        creditScore: "Credit Score:",
        balance: "Account Balance:",
        debt: "Total Debt:",
        credits: "Active Credits:",
        latePay: "Late Payments:",
        txCount: "Monthly Transactions:",
        products: "Existing Products:",
        runAiBtn: "Run AI Analysis (Risk & Offer)",
        analyzingBtn: "Analyzing...",
        welcomeHeader: "Decision Engine Ready",
        welcomeDesc: "Select a customer from the left list to initiate real-time analysis.",
        creditRisk: "CREDIT RISK",
        fraudRisk: "FRAUD RISK",
        churnRisk: "CHURN RISK",
        recProductHeader: "Personalized Product Offer",
        recScoreLabel: "Fit Score",
        xaiHeader: "Why Was This Offer Recommended? (Explainability)",
        summaryHeader: "AI Decision Summary:",
        staffLogin: "Staff Login",
        modalTitle: "Staff Access Portal",
        modalDesc: "You may authenticate with your staff credentials (Optional).",
        lblStaffId: "Staff ID / Badge No",
        lblPassword: "Password",
        lblRole: "Department / Role",
        roleCredit: "Credit Underwriter",
        roleRisk: "Risk Analyst",
        roleManager: "Branch Manager",
        roleRep: "Customer Representative",
        btnClose: "Close",
        btnLogin: "Sign In",
        noJob: "Not Specified",
        none: "None",
        noResult: "No records found."
    }
};

// Meslek Çevirileri
const occupationMap = {
    "Mimar": "Architect",
    "Bankaci": "Banker",
    "Muhasebeci": "Accountant",
    "Ogretmen": "Teacher",
    "Avukat": "Lawyer",
    "Doktor": "Doctor",
    "Hemsire": "Nurse",
    "Muhendis": "Engineer",
    "Yazilim Muhendisi": "Software Engineer",
    "Esnaf": "Tradesman",
    "Pazarlamaci": "Marketer",
    "Emekli": "Retired",
    "Ogrenci": "Student"
};

// Tekil Ürün Çevirileri
const singleProductMap = {
    "Vadesiz TL": "Demand Deposit Account",
    "Ek Hesap": "Overdraft Account",
    "Kredi Karti": "Credit Card",
    "Yuksek Getirili Vadeli Mevduat": "High-Yield Time Deposit",
    "Bireysel Emeklilik Sistemi (BES)": "Personal Pension Scheme (PPS)",
    "Ihtiyac Kredisi": "Consumer Loan",
    "Konut Kredisi": "Mortgage Loan",
    "Tasit Kredisi": "Vehicle Loan",
    "Platinum / Gold Kredi Karti": "Platinum / Gold Credit Card",
    "Kredi Karti Limit Artisi": "Credit Card Limit Increase",
    "Yatirim Fonu": "Mutual Investment Fund"
};

// XAI Gerekçe Cümleleri Çevirileri
const reasonMap = {
    "yuksek gelir ve yuksek kredi guvenirligi": "High income and high creditworthiness.",
    "yuksek gelir ve yuksek kredi guvenilirligi": "High income and high creditworthiness.",
    "gecmis donemde 3 adet gecikmeli odeme kaydi bulundu": "3 late payment records were detected in previous periods.",
    "vadesiz hesapta atil duran yuksek nakit bakiyesi": "High idle cash balance detected in checking account.",
    "uzun vadeli birikim ve vergi avantaji profiline uygunluk": "Suitable for long-term savings and tax advantage profile.",
    "kredi karti limit kullanim orani kritik esigin uzerinde": "Credit card limit utilization rate is above critical threshold (90%+).",
    "yuksek kredi skoru ve duzenli geri odeme gecmisi": "High credit score and consistent repayment history."
};

function translateOccupation(val) {
    if (!val) return dict[currentLang].noJob;
    return currentLang === "en" ? (occupationMap[val] || val) : val;
}

function translateProductsString(productsStr) {
    if (!productsStr) return dict[currentLang].none;
    if (currentLang === "tr") return productsStr;

    const items = productsStr.split(',').map(item => item.trim());
    const translatedItems = items.map(item => singleProductMap[item] || item);
    return translatedItems.join(', ');
}

function translateReason(val) {
    if (!val || currentLang === "tr") return val;
    const cleanStr = val.toLowerCase().replace(/[.\n\r]/g, '').trim();
    for (let key in reasonMap) {
        if (cleanStr.includes(key)) {
            return reasonMap[key];
        }
    }
    return val;
}

// --- DOM HAZIR OLDUĞUNDA ---
document.addEventListener("DOMContentLoaded", () => {
    loadCustomers();
    document.getElementById("btnAnalyze").addEventListener("click", runAnalysis);

    // Müşteri Arama
    document.getElementById("searchInput").addEventListener("input", (e) => {
        const q = e.target.value.toLowerCase().trim();
        filteredCustomers = allCustomers.filter(cust =>
            (cust.fullName && cust.fullName.toLowerCase().includes(q)) ||
            (cust.occupation && cust.occupation.toLowerCase().includes(q)) ||
            (cust.identityNumber && cust.identityNumber.includes(q)) ||
            (cust.id && cust.id.toString() === q)
        );
        renderLimit = 30;
        document.getElementById("custCountBadge").innerText = filteredCustomers.length;
        renderCustomerRows();
    });

    // Kutu içi aşağı kaydırma (Infinite Scroll)
    const scrollBox = document.getElementById("tableScrollArea");
    scrollBox.addEventListener("scroll", () => {
        if (scrollBox.scrollTop + scrollBox.clientHeight >= scrollBox.scrollHeight - 30) {
            if (renderLimit < filteredCustomers.length) {
                renderLimit += 30;
                renderCustomerRows();
            }
        }
    });

    // Dil Seçeneği Butonu
    document.getElementById("btnLanguage").addEventListener("click", () => {
        currentLang = currentLang === "tr" ? "en" : "tr";
        document.getElementById("langText").innerText = currentLang === "tr" ? "EN" : "TR";

        applyTranslations();
        renderCustomerRows();

        if (selectedCustomerId) {
            selectCustomer(selectedCustomerId, false);
        }

        if (lastAnalysisData) {
            renderAnalysisData(lastAnalysisData);
        }
    });

    // Personel Girişi
    document.getElementById("btnLoginSave").addEventListener("click", () => {
        const idVal = document.getElementById("staffIdInput").value.trim();
        const roleVal = document.getElementById("staffRoleSelect").value;
        if (idVal) {
            document.getElementById("staffLabel").innerText = `${idVal} (${roleVal})`;
        }
        const modalEl = document.getElementById("staffModal");
        const modal = bootstrap.Modal.getInstance(modalEl);
        if (modal) modal.hide();
    });
});

// --- MÜŞTERİLERİ YÜKLE ---
async function loadCustomers() {
    try {
        const res = await fetch("/api/customers");
        allCustomers = await res.json();
        filteredCustomers = [...allCustomers];
        document.getElementById("custCountBadge").innerText = allCustomers.length;
        renderCustomerRows();
    } catch (err) {
        console.error("Müşteri listesi alınamadı:", err);
        document.getElementById("customerTableBody").innerHTML = `<tr><td colspan="4" class="text-danger py-3">Müşteriler yüklenemedi.</td></tr>`;
    }
}

// --- TABLO ÇİZİMİ ---
function renderCustomerRows() {
    const tbody = document.getElementById("customerTableBody");
    tbody.innerHTML = "";
    const t = dict[currentLang];

    if (filteredCustomers.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" class="text-muted py-3">${t.noResult}</td></tr>`;
        return;
    }

    const items = filteredCustomers.slice(0, renderLimit);
    const fragment = document.createDocumentFragment();

    items.forEach(cust => {
        const tr = document.createElement("tr");
        if (cust.id === selectedCustomerId) {
            tr.classList.add("selected-customer-row");
        }
        tr.innerHTML = `
            <td>${cust.id}</td>
            <td class="fw-semibold">${cust.fullName}</td>
            <td>${translateOccupation(cust.occupation)}</td>
            <td>
                <button class="btn btn-sm btn-outline-primary" onclick="selectCustomer(${cust.id})">
                    ${t.selectBtn}
                </button>
            </td>
        `;
        fragment.appendChild(tr);
    });

    tbody.appendChild(fragment);
}

// --- MÜŞTERİ SEÇİMİ ---
async function selectCustomer(id, resetView = true) {
    try {
        const res = await fetch(`/api/customers/${id}`);
        const cust = await res.json();
        selectedCustomerId = cust.id;

        renderCustomerRows();

        const t = dict[currentLang];
        document.getElementById("custIdentity").innerText = cust.identityNumber;
        document.getElementById("custFullName").innerText = `${cust.fullName} (${cust.age}, ${translateOccupation(cust.occupation)})`;
        document.getElementById("custIncome").innerText = Number(cust.monthlyIncome).toLocaleString('tr-TR');
        document.getElementById("custExpenses").innerText = Number(cust.monthlyExpenses).toLocaleString('tr-TR');
        document.getElementById("custCreditScore").innerText = cust.creditScore;
        document.getElementById("custBalance").innerText = Number(cust.accountBalance).toLocaleString('tr-TR');
        document.getElementById("custDebt").innerText = Number(cust.totalDebt).toLocaleString('tr-TR');
        document.getElementById("custCreditsCount").innerText = cust.activeCreditsCount || 0;
        document.getElementById("custLatePayments").innerText = cust.latePaymentsCount || 0;
        document.getElementById("custTransactions").innerText = cust.monthlyTransactionCount || 0;
        document.getElementById("custProducts").innerText = translateProductsString(cust.existingProducts);

        document.getElementById("customerDetailCard").style.display = "block";

        if (resetView) {
            document.getElementById("welcomePlaceholder").style.display = "block";
            document.getElementById("analysisResultArea").style.display = "none";
            lastAnalysisData = null;
        }
    } catch (err) {
        console.error("Müşteri detayı alınamadı:", err);
    }
}

// --- AI ANALİZİ ÇALIŞTIR ---
async function runAnalysis() {
    if (!selectedCustomerId) return;

    const t = dict[currentLang];
    const btn = document.getElementById("btnAnalyze");
    btn.disabled = true;
    btn.innerHTML = `<span class="spinner-border spinner-border-sm me-2"></span>${t.analyzingBtn}`;

    try {
        const res = await fetch(`/api/customers/${selectedCustomerId}/analyze`, {
            method: "POST"
        });
        const data = await res.json();
        lastAnalysisData = data;

        renderAnalysisData(data);

        document.getElementById("welcomePlaceholder").style.display = "none";
        document.getElementById("analysisResultArea").style.display = "block";

    } catch (err) {
        alert("AI Analizi sırasında hata oluştu. FastAPI servisinin açık olduğundan emin olun.");
        console.error(err);
    } finally {
        btn.disabled = false;
        btn.innerHTML = `<i class="bi bi-cpu me-2"></i><span data-i18n="runAiBtn">${t.runAiBtn}</span>`;
    }
}

// --- ANALİZ VERİLERİNİ EKRANA BAS (OKLAR VE DİL DESTEĞİ) ---
function renderAnalysisData(data) {
    updateRiskCard("creditRisk", data.creditRisk);
    updateRiskCard("fraudRisk", data.fraudRisk);
    updateRiskCard("churnRisk", data.churnRisk);

    // 1. Önerilen Ürün
    document.getElementById("recProductTitle").innerText = translateProductsString(data.recommendedProduct);
    document.getElementById("recProductScore").innerText = `%${data.recommendationScore}`;

    // 2. Karar Gerekçeleri (İngilizceye Çevrilir)
    const reasonsList = document.getElementById("reasonsList");
    reasonsList.innerHTML = "";
    data.explanationReasons.forEach(reason => {
        const li = document.createElement("li");
        li.className = "list-group-item px-0 text-secondary bg-transparent";
        li.innerHTML = `<i class="bi bi-check2-circle text-success me-2"></i>${translateReason(reason)}`;
        reasonsList.appendChild(li);
    });

    // 3. Doğal Dil Karar Özeti
    const summaryEl = document.getElementById("naturalLanguageSummary");
    if (currentLang === "en") {
        const fullName = document.getElementById("custFullName").innerText.split('(')[0].trim();
        const engProduct = translateProductsString(data.recommendedProduct);
        summaryEl.innerText = `In the analysis conducted for customer ${fullName}; Credit Risk was identified as %${data.creditRisk.probability} (${data.creditRisk.level}), Fraud Risk as %${data.fraudRisk.probability} (${data.fraudRisk.level}), and Churn Risk as %${data.churnRisk.probability} (${data.churnRisk.level}). In accordance with the customer's financial indicators, the most suitable offer is '${engProduct}' with a fit score of %${data.recommendationScore}.`;
    } else {
        summaryEl.innerText = data.aiNaturalLanguageSummary;
    }
}

// --- RİSK ROZETLERİ (OK İŞARETLERİ VE SEVİYELER) ---
function updateRiskCard(prefix, riskObj) {
    document.getElementById(`${prefix}Prob`).innerText = `%${riskObj.probability}`;
    const badge = document.getElementById(`${prefix}Badge`);

    const lvl = (riskObj.level || "").toUpperCase();
    let displayText = "";

    if (lvl === "HIGH") {
        displayText = currentLang === "tr" ? "▲ YÜKSEK" : "▲ HIGH";
    } else if (lvl === "MEDIUM") {
        displayText = currentLang === "tr" ? "► ORTA" : "► MEDIUM";
    } else {
        displayText = currentLang === "tr" ? "▼ DÜŞÜK" : "▼ LOW";
    }

    badge.innerText = displayText;
    badge.className = `badge bg-${riskObj.statusColor} px-2 py-1`;
}

// --- STATİK METİNLERİ ÇEVİR ---
function applyTranslations() {
    const t = dict[currentLang];
    document.querySelectorAll("[data-i18n]").forEach(el => {
        const key = el.getAttribute("data-i18n");
        if (t[key]) el.innerText = t[key];
    });
    document.querySelectorAll("[data-i18n-ph]").forEach(el => {
        const key = el.getAttribute("data-i18n-ph");
        if (t[key]) el.placeholder = t[key];
    });
}