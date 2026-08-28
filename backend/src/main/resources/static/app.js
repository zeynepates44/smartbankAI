let currentPage = 0;
const pageSize = 20;
let totalPages = 1;
let totalItems = 0;
let allCustomers = [];
let selectedCustomerId = null;
let isLoading = false;
let isSearchMode = false;

document.addEventListener("DOMContentLoaded", () => {
    initTheme();
    loadCustomers(currentPage, true);
    setupScrollListener();
    renderIcons();
});

// 1. Tema Yönetimi
function initTheme() {
    const savedTheme = localStorage.getItem("theme") || "dark";
    applyTheme(savedTheme);
}

function toggleTheme() {
    const isDark = document.documentElement.classList.contains("dark");
    const newTheme = isDark ? "light" : "dark";
    applyTheme(newTheme);
    localStorage.setItem("theme", newTheme);
}

function applyTheme(theme) {
    const icon = document.getElementById("theme-icon");
    if (theme === "dark") {
        document.documentElement.classList.add("dark");
        if (icon) icon.setAttribute("data-lucide", "sun");
    } else {
        document.documentElement.classList.remove("dark");
        if (icon) icon.setAttribute("data-lucide", "moon");
    }
    renderIcons();
}

// 2. Personel Giriş Modalı
function toggleLoginModal(show) {
    const modal = document.getElementById("login-modal");
    if (modal) {
        if (show) modal.classList.remove("hidden");
        else modal.classList.add("hidden");
    }
    renderIcons();
}

function handleLogin(event) {
    event.preventDefault();
    toggleLoginModal(false);
    showToast("Personel girişi başarılı! Hoş geldiniz.", "success");
}

// 3. API'den Veri Çekme (Sayfa Ekleme / Sonsuz Kaydırma)
async function loadCustomers(page = 0, isInitial = false) {
    if (isLoading) return;
    if (!isInitial && page >= totalPages) return;

    isLoading = true;
    updateScrollStatus(true);

    try {
        const response = await fetch(`/api/customers?page=${page}&size=${pageSize}`);
        if (!response.ok) throw new Error("Veri çekilemedi (" + response.status + ")");

        const data = await response.json();
        currentPage = data.currentPage;
        totalPages = data.totalPages;
        totalItems = data.totalItems;

        if (isInitial) {
            allCustomers = data.customers || [];
        } else {
            allCustomers = [...allCustomers, ...(data.customers || [])];
        }

        renderTable(allCustomers);
        updateCountSummary();
    } catch (error) {
        console.error("Yükleme hatası:", error);
        showToast(error.message, "error");
    } finally {
        isLoading = false;
        updateScrollStatus(false);
    }
}

// 4. Tabloyu Doldurma
function renderTable(customers) {
    const tableBody = document.getElementById("customer-table-body");
    if (!tableBody) return;

    if (!customers || customers.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="6" class="text-center py-8 text-slate-400">Sonuç bulunamadı.</td></tr>`;
        return;
    }

    tableBody.innerHTML = customers.map(c => {
        const isSelected = selectedCustomerId === c.customerId;
        const rowClass = isSelected
            ? "bg-cyan-500/10 dark:bg-cyan-500/20 font-semibold"
            : "hover:bg-slate-100/60 dark:hover:bg-slate-800/50";

        return `
            <tr class="transition cursor-pointer ${rowClass}" onclick="selectCustomer(${c.customerId})">
                <td class="p-3 font-bold text-slate-900 dark:text-slate-100">#${c.customerId}</td>
                <td class="p-3">${c.age || '-'}</td>
                <td class="p-3">${formatCurrency(c.monthlyIncome)}</td>
                <td class="p-3">
                    <span class="px-2 py-0.5 rounded text-[11px] font-bold ${getScoreBadge(c.creditScore)}">
                        ${c.creditScore || '-'}
                    </span>
                </td>
                <td class="p-3 text-emerald-600 dark:text-emerald-400 font-medium">${formatCurrency(c.accountBalance)}</td>
                <td class="p-3 text-right">
                    <button onclick="event.stopPropagation(); selectCustomer(${c.customerId})" class="px-2.5 py-1 text-[11px] font-semibold rounded-lg bg-cyan-600/10 hover:bg-cyan-600/20 text-cyan-600 dark:text-cyan-400 transition">
                        Seç & Analiz Et
                    </button>
                </td>
            </tr>
        `;
    }).join("");
}

// 5. Sonsuz Kaydırma Dinleyicisi
function setupScrollListener() {
    const scrollContainer = document.querySelector(".custom-scrollbar");
    if (!scrollContainer) return;

    scrollContainer.addEventListener("scroll", () => {
        if (isSearchMode) return; // Arama yapılıyorken sonsuz kaydırmayı durdur

        const { scrollTop, scrollHeight, clientHeight } = scrollContainer;
        // Tablonun altına 60px kala yeni sayfayı çek
        if (scrollTop + clientHeight >= scrollHeight - 60) {
            if (!isLoading && currentPage + 1 < totalPages) {
                loadCustomers(currentPage + 1, false);
            }
        }
    });
}

// 6. Doğrudan Backend Destekli ID Arama
let searchTimeout = null;
async function filterCustomers() {
    const input = document.getElementById("search-input");
    if (!input) return;
    const query = input.value.trim();

    clearTimeout(searchTimeout);

    if (!query) {
        isSearchMode = false;
        renderTable(allCustomers);
        updateCountSummary();
        return;
    }

    // Kullanıcı yazmayı bitirdikten 250ms sonra ara
    searchTimeout = setTimeout(async () => {
        isSearchMode = true;

        // Önce bellekteki yüklü müşterilere bak
        const localMatches = allCustomers.filter(c => c.customerId.toString().includes(query));
        if (localMatches.length > 0) {
            renderTable(localMatches);
            const countEl = document.getElementById("loaded-count");
            if (countEl) countEl.innerText = `${localMatches.length} Eşleşen Müşteri`;
            return;
        }

        // Bellekte yoksa doğrudan backend'den ID ile çek
        if (!isNaN(query)) {
            try {
                const res = await fetch(`/api/customers/${query}`);
                if (res.ok) {
                    const cust = await res.json();
                    renderTable([cust]);
                    const countEl = document.getElementById("loaded-count");
                    if (countEl) countEl.innerText = `1 Eşleşen Müşteri`;
                    return;
                }
            } catch (e) {
                // Bulunamadıysa aşağıda boş liste basacak
            }
        }

        renderTable([]);
        const countEl = document.getElementById("loaded-count");
        if (countEl) countEl.innerText = `0 Eşleşen Müşteri`;
    }, 250);
}

// 7. Müşteri Seçimi ve Sağ Panel AI Analizi
async function selectCustomer(id) {
    selectedCustomerId = id;
    renderTable(isSearchMode ? [allCustomers.find(c => c.customerId === id) || { customerId: id }] : allCustomers);

    const loader = document.getElementById("ai-loader");
    if (loader) loader.classList.remove("hidden");

    try {
        const res = await fetch(`/api/customers/${id}/recommendation`);
        if (!res.ok) {
            const errJson = await res.json();
            throw new Error(errJson.message || "AI servisi yanıt vermedi.");
        }

        const data = await res.json();
        updateRightPanel(data);
    } catch (err) {
        console.error("AI Hatası:", err);
        showToast(err.message, "error");
    } finally {
        if (loader) loader.classList.add("hidden");
    }
}

// 8. Sağ Paneli Güncelleme
function updateRightPanel(data) {
    const cust = data.customer || {};
    const ai = data.aiRecommendation || {};

    setText("panel-customer-id", `Müşteri #${cust.customerId}`);
    setText("panel-customer-age", `Yaş: ${cust.age || '-'}`);
    setText("panel-offer", formatOffer(ai.recommended_offer));

    const confPct = Math.round((ai.confidence || 0) * 100);
    setText("panel-confidence", `%${confPct}`);

    const progressBar = document.getElementById("panel-progress");
    if (progressBar) progressBar.style.width = `${confPct}%`;

    const tag = document.getElementById("panel-tag");
    if (tag) {
        if (ai.is_fallback) {
            tag.innerText = "KURAL MOTORU (FALLBACK)";
            tag.className = "text-[10px] font-semibold px-2 py-0.5 rounded bg-amber-500/10 text-amber-500 border border-amber-500/30";
        } else {
            tag.innerText = "CANLI ML TAHMİNİ";
            tag.className = "text-[10px] font-semibold px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-500 border border-emerald-500/30";
        }
    }

    setText("panel-income-expense", `${formatCurrency(cust.monthlyIncome)} / ${formatCurrency(cust.monthlyExpense)}`);
    setText("panel-debt", formatCurrency(cust.debtAmount));
    setText("panel-balance", formatCurrency(cust.accountBalance));
    setText("panel-late-payment", `${cust.latePaymentCount || 0} Adet`);
}

// Durum Yardımcıları
function updateScrollStatus(loading) {
    const status = document.getElementById("scroll-status");
    if (!status) return;
    status.innerText = loading ? "Daha fazla müşteri yükleniyor..." : "Aşağı kaydırdıkça yeni müşteriler yüklenir";
}

function updateCountSummary() {
    const countEl = document.getElementById("loaded-count");
    if (countEl) {
        countEl.innerText = `${allCustomers.length} / ${totalItems} Müşteri Yüklendi`;
    }
}

function showToast(message, type = "info") {
    const container = document.getElementById("toast-container");
    if (!container) return;

    const toast = document.createElement("div");
    const bgClass = type === "error" ? "bg-rose-500 text-white" : "bg-emerald-600 text-white";
    toast.className = `px-4 py-3 rounded-xl shadow-xl text-xs font-semibold flex items-center gap-2 transition-all transform duration-300 ${bgClass}`;
    toast.innerHTML = `<span>${message}</span>`;

    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = "0";
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

function setText(id, text) {
    const el = document.getElementById(id);
    if (el) el.innerText = text;
}

function formatCurrency(val) {
    if (val == null) return "0 ₺";
    return new Intl.NumberFormat("tr-TR", { style: "currency", currency: "TRY", maximumFractionDigits: 0 }).format(val);
}

function getScoreBadge(score) {
    if (!score) return "bg-slate-200 dark:bg-slate-800 text-slate-400";
    if (score >= 700) return "bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border border-emerald-500/20";
    if (score >= 600) return "bg-cyan-500/10 text-cyan-600 dark:text-cyan-400 border border-cyan-500/20";
    if (score >= 500) return "bg-amber-500/10 text-amber-600 dark:text-amber-400 border border-amber-500/20";
    return "bg-rose-500/10 text-rose-600 dark:text-rose-400 border border-rose-500/20";
}

function formatOffer(offer) {
    if (!offer) return "-";
    switch (offer) {
        case "KREDI": return "İhtiyaç Kredisi Teklifi";
        case "KREDI_KARTI": return "Özel Kredi Kartı Teklifi";
        case "YATIRIM": return "Yatırım & Portföy Teklifi";
        case "TEKLIF_YOK": return "Şu An Teklif Yok";
        default: return offer.replace("_", " ");
    }
}

function renderIcons() {
    if (window.lucide) window.lucide.createIcons();
}