let allCustomers = [];

// Tanımlı Yetkili Personel Veritabanı
const authorizedStaff = [
    { role: "Bireysel Müşteri Temsilcisi", id: "SB-1001", pass: "bireysel2026", title: "Müşteri Temsilcisi" },
    { role: "Kredi Tahsis Uzmanı", id: "SB-2002", pass: "tahsis2026", title: "Kredi Tahsis Uzmanı" },
    { role: "Finansal Risk Analisti", id: "SB-3003", pass: "analist2026", title: "Risk Analisti" },
    { role: "Sistem Yöneticisi (Admin)", id: "SB-9999", pass: "admin2026", title: "Admin" }
];

// Tema Yönetimi
function initTheme() {
    const saved = localStorage.getItem('sb-theme') || 'light';
    setTheme(saved);
}

function toggleTheme() {
    const current = document.documentElement.classList.contains('dark') ? 'dark' : 'light';
    setTheme(current === 'dark' ? 'light' : 'dark');
}

function setTheme(t) {
    const el = document.documentElement;
    const icon = document.getElementById('themeIcon');
    if (t === 'dark') {
        el.classList.add('dark');
        el.classList.remove('light');
        if (icon) icon.className = 'fa-solid fa-sun text-amber-400 text-sm';
        localStorage.setItem('sb-theme', 'dark');
    } else {
        el.classList.remove('dark');
        el.classList.add('light');
        if (icon) icon.className = 'fa-solid fa-moon text-slate-600 text-sm';
        localStorage.setItem('sb-theme', 'light');
    }
}

// Personel Oturum Yönetimi
function openLoginModal() {
    const modal = document.getElementById('loginModal');
    if (modal) modal.classList.remove('hidden');
}

function closeLoginModal() {
    const modal = document.getElementById('loginModal');
    if (modal) modal.classList.add('hidden');
}

function handleLogin(e) {
    e.preventDefault();
    const role = document.getElementById('staffRole').value;
    const staffId = document.getElementById('staffId').value.trim().toUpperCase();
    const staffPass = document.getElementById('staffPassword').value.trim();
    const errBox = document.getElementById('loginAuthError');

    // Sicil, Şifre ve Rol Doğrulama
    const matchedUser = authorizedStaff.find(
        u => u.id === staffId && u.pass === staffPass && u.role === role
    );

    if (!matchedUser) {
        errBox.classList.remove('hidden');
        errBox.innerText = 'Geçersiz Sicil No, Şifre veya Rol seçimi!';
        return;
    }

    errBox.classList.add('hidden');
    const user = {
        id: matchedUser.id,
        role: matchedUser.role,
        title: matchedUser.title
    };

    localStorage.setItem('sb-user', JSON.stringify(user));
    document.getElementById('loginForm').reset();
    closeLoginModal();
    renderUserAuth();
}

function handleLogout() {
    localStorage.removeItem('sb-user');
    renderUserAuth();
}

function renderUserAuth() {
    const authBox = document.getElementById('authContainer');
    if (!authBox) return;

    const userStr = localStorage.getItem('sb-user');

    if (!userStr) {
        authBox.innerHTML = `
            <button onclick="openLoginModal()" class="flex items-center space-x-2 px-3.5 py-2 rounded-xl bg-slate-100 hover:bg-slate-200 dark:bg-slate-800 dark:hover:bg-slate-700 text-slate-700 dark:text-slate-200 text-xs font-bold border border-slate-200 dark:border-slate-700 transition active:scale-95 shadow-sm">
                <i class="fa-regular fa-user text-sm text-sky-600 dark:text-sky-400"></i>
                <span>Personel Girişi</span>
            </button>
        `;
        return;
    }

    const user = JSON.parse(userStr);
    authBox.innerHTML = `
        <div class="flex items-center space-x-2.5 bg-slate-100/90 dark:bg-slate-800/90 border border-slate-200/80 dark:border-slate-700/80 px-3 py-1.5 rounded-xl shadow-sm">
            <div class="w-7 h-7 rounded-lg bg-sky-600 text-white flex items-center justify-center text-xs font-bold shadow-sm">
                <i class="fa-solid fa-user-shield text-[11px]"></i>
            </div>
            <div class="text-left">
                <p class="text-[11px] font-extrabold text-slate-800 dark:text-slate-100 leading-tight">${user.id}</p>
                <p class="text-[9px] font-bold text-sky-600 dark:text-sky-400 uppercase tracking-tight">${user.title}</p>
            </div>
            <button onclick="handleLogout()" title="Oturumu Kapat" class="ml-1 text-slate-400 hover:text-rose-600 dark:hover:text-rose-400 text-xs p-1 transition">
                <i class="fa-solid fa-arrow-right-from-bracket"></i>
            </button>
        </div>
    `;
}

// Uygulama Başlatma
document.addEventListener('DOMContentLoaded', () => {
    initTheme();
    renderUserAuth();
    fetchCustomers();
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', handleSearch);
    }
});

async function fetchCustomers() {
    try {
        const response = await fetch('/api/customers');
        if (!response.ok) throw new Error('Veri çekilemedi');
        allCustomers = await response.json();

        calculateMetrics(allCustomers);
        renderCustomerTable(allCustomers.slice(0, 100));
    } catch (err) {
        document.getElementById('customerTableBody').innerHTML = `
            <tr><td colspan="6" class="text-center py-10 text-rose-500 font-semibold">Müşteri listesi yüklenemedi. Backend servisinin açık olduğunu kontrol edin.</td></tr>
        `;
    }
}

function calculateMetrics(data) {
    if (!data || !data.length) return;

    const total = data.length;
    const totalScore = data.reduce((acc, c) => acc + (c.creditScore || 0), 0);
    const avgScore = Math.round(totalScore / total);

    const riskyCount = data.filter(c => (c.latePaymentCount > 0) || (c.creditScore < 600)).length;
    const riskRatio = ((riskyCount / total) * 100).toFixed(1);

    const totalBalance = data.reduce((acc, c) => acc + (c.accountBalance || 0), 0);
    const avgBalance = Math.round(totalBalance / total);

    document.getElementById('stat-total-customers').innerText = total.toLocaleString();
    document.getElementById('stat-avg-score').innerText = avgScore;
    document.getElementById('stat-risk-ratio').innerText = `%${riskRatio}`;
    document.getElementById('stat-avg-balance').innerText = `${avgBalance.toLocaleString()} ₺`;
}

function renderCustomerTable(list) {
    const tbody = document.getElementById('customerTableBody');
    tbody.innerHTML = '';

    if (!list.length) {
        tbody.innerHTML = `<tr><td colspan="6" class="text-center py-10 text-slate-400 font-medium">Eşleşen kayıt bulunamadı.</td></tr>`;
        return;
    }

    list.forEach(c => {
        const id = c.customerId ?? c.id;
        const score = c.creditScore || 0;
        let badgeCls = score >= 750 ? 'bg-emerald-100 text-emerald-800 dark:bg-emerald-950 dark:text-emerald-300' :
                       score >= 600 ? 'bg-blue-100 text-blue-800 dark:bg-blue-950 dark:text-blue-300' :
                       'bg-rose-100 text-rose-800 dark:bg-rose-950 dark:text-rose-300';

        const tr = document.createElement('tr');
        tr.className = "hover:bg-slate-100/60 dark:hover:bg-slate-800/40 transition border-b border-slate-100 dark:border-slate-800/60";
        tr.innerHTML = `
            <td class="px-4 py-3 font-bold text-slate-900 dark:text-white">#${id}</td>
            <td class="px-4 py-3">${c.age}</td>
            <td class="px-4 py-3 font-semibold">${Number(c.monthlyIncome || 0).toLocaleString()} ₺</td>
            <td class="px-4 py-3"><span class="px-2.5 py-0.5 rounded-md font-bold text-[11px] ${badgeCls}">${score}</span></td>
            <td class="px-4 py-3 text-emerald-600 dark:text-emerald-400 font-semibold">${Number(c.accountBalance || 0).toLocaleString()} ₺</td>
            <td class="px-4 py-3 text-right">
                <button id="btn-analyze-${id}" onclick="analyzeCustomer(${id})" class="bg-gradient-to-r from-sky-600 to-blue-600 hover:from-sky-500 hover:to-blue-500 text-white font-bold text-xs px-3.5 py-1.5 rounded-xl shadow-sm hover:shadow transition inline-flex items-center gap-1.5 active:scale-95">
                    <i class="fa-solid fa-bolt text-[10px]"></i> AI Analiz
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function handleSearch(e) {
    const q = e.target.value.toLowerCase().trim();
    const filtered = allCustomers.filter(c => {
        const cid = (c.customerId ?? c.id ?? '').toString();
        const sc = (c.creditScore ?? '').toString();
        return cid.includes(q) || sc.includes(q);
    });
    renderCustomerTable(filtered.slice(0, 100));
}

async function analyzeCustomer(customerId) {
    const btn = document.getElementById(`btn-analyze-${customerId}`);
    const orig = btn ? btn.innerHTML : '';
    if (btn) {
        btn.disabled = true;
        btn.innerHTML = `<i class="fa-solid fa-spinner animate-spin"></i>`;
    }

    document.getElementById('aiPlaceholder').classList.add('hidden');
    document.getElementById('aiResultCard').classList.add('hidden');
    document.getElementById('aiErrorCard').classList.add('hidden');
    document.getElementById('aiSkeleton').classList.remove('hidden');

    try {
        const res = await fetch(`/api/customers/${customerId}/recommendation`);
        if (!res.ok) throw new Error('AI servisi yanıt vermedi.');
        const data = await res.json();

        const c = data.customer;
        const ai = data.aiRecommendation;
        const id = c.customerId ?? c.id ?? customerId;

        document.getElementById('aiSkeleton').classList.add('hidden');
        document.getElementById('aiResultCard').classList.remove('hidden');

        document.getElementById('resCustomerId').innerText = `Müşteri #${id}`;
        document.getElementById('resAge').innerText = `Yaş: ${c.age}`;
        document.getElementById('resOffer').innerText = ai.recommended_offer;

        const conf = Math.round((ai.confidence || 0) * 100);
        document.getElementById('resConfidence').innerText = `%${conf}`;
        document.getElementById('resConfidenceBar').style.width = `${conf}%`;

        const cardBox = document.getElementById('offerCardBox');
        const badge = document.getElementById('offerStatusBadge');
        const confBar = document.getElementById('resConfidenceBar');

        if (ai.recommended_offer === 'TEKLIF_YOK') {
            cardBox.className = 'p-5 rounded-2xl border bg-rose-50/70 border-rose-200 dark:bg-rose-950/20 dark:border-rose-900/60 text-rose-950 dark:text-rose-200';
            badge.className = 'text-[10px] font-bold uppercase px-2 py-0.5 rounded-md bg-rose-200 text-rose-800 dark:bg-rose-900 dark:text-rose-300';
            badge.innerText = 'Riskli Profil';
            confBar.className = 'h-2.5 rounded-full bg-rose-500 transition-all duration-700';
        } else {
            cardBox.className = 'p-5 rounded-2xl border bg-gradient-to-br from-sky-50 to-blue-50 border-sky-200 dark:from-sky-950/30 dark:to-blue-950/30 dark:border-sky-800/60 text-slate-900 dark:text-white';
            badge.className = 'text-[10px] font-bold uppercase px-2 py-0.5 rounded-md bg-emerald-100 text-emerald-800 dark:bg-emerald-950 dark:text-emerald-300';
            badge.innerText = 'Uygun Teklif';
            confBar.className = 'h-2.5 rounded-full bg-sky-500 transition-all duration-700';
        }

        document.getElementById('resIncomeExpense').innerText = `${Number(c.monthlyIncome || 0).toLocaleString()} ₺ / ${Number(c.monthlyExpense || 0).toLocaleString()} ₺`;
        document.getElementById('resDebt').innerText = `${Number(c.debtAmount || 0).toLocaleString()} ₺`;
        document.getElementById('resBalance').innerText = `${Number(c.accountBalance || 0).toLocaleString()} ₺`;

        const lateEl = document.getElementById('resLatePayment');
        const lateCount = c.latePaymentCount ?? 0;
        lateEl.innerText = `${lateCount} Adet`;
        lateEl.className = lateCount > 0 ? 'font-bold px-2 py-0.5 rounded text-xs bg-rose-100 text-rose-700 dark:bg-rose-950 dark:text-rose-400' : 'font-bold px-2 py-0.5 rounded text-xs bg-emerald-100 text-emerald-700 dark:bg-emerald-950 dark:text-emerald-400';

    } catch (error) {
        document.getElementById('aiSkeleton').classList.add('hidden');
        document.getElementById('aiErrorCard').classList.remove('hidden');
        document.getElementById('aiErrorMessage').innerText = error.message || 'Yapay zekâ servisi ile iletişim kurulamadı.';
    } finally {
        if (btn) {
            btn.disabled = false;
            btn.innerHTML = orig;
        }
    }
}