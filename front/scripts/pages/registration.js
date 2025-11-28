const root = document.documentElement;
const themeToggle = document.getElementById("themeToggle");
const themeIcon = document.getElementById("themeIcon");

function applyTheme(mode) {
  root.classList.remove("theme-light", "theme-dark");
  if (mode === "light") root.classList.add("theme-light");
  if (mode === "dark") root.classList.add("theme-dark");

  if (themeIcon) {
    themeIcon.src = mode === "dark" ? "../icons/moon.ico" : "../icons/sun.ico";
  }
  localStorage.setItem("GantHedgehog_theme", mode);
}

function initTheme() {
  const stored = localStorage.getItem("GantHedgehog_theme");
  if (stored) return applyTheme(stored);
  applyTheme("dark");
}

initTheme();

themeToggle.addEventListener("click", () => {
  applyTheme(root.classList.contains("theme-light") ? "dark" : "light");
});

const form = document.getElementById("registrationForm");
const nameEl = document.getElementById("name");
const emailEl = document.getElementById("email");
const passEl = document.getElementById("password");
const pass2El = document.getElementById("confirmPassword");
const loginBtn = document.getElementById("loginButton");
const submitBtn = form.querySelector(".btn.primary");

form.addEventListener("submit", (e) => {
  e.preventDefault();
  
  const name = nameEl.value.trim();
  const email = emailEl.value.trim();
  const pass = passEl.value;
  const pass2 = pass2El.value;

  if (!name || !email || !pass || !pass2) {
    alert("Заполните все поля.");
    return;
  }

  if (pass !== pass2) {
    alert("Пароли не совпадают.");
    return;
  }

  /* Отправка данных в бэк, наверно, нужна обработка ошибок от бэка...
  пусть отпределит ромчик
  */
 
  const result = true;

  submitBtn.disabled = true;

  setTimeout(() => {
    alert("Аккаунт создан...");
    submitBtn.disabled = false;
    window.location.href = "./auth.html";
  }, 700);
});

loginBtn.addEventListener("click", () => {
  window.location.href = "./auth.html";
});

document.querySelectorAll('.toggle-password').forEach(btn => {
  btn.addEventListener('click', () => {
    const input = btn.parentElement.querySelector('input');
    const img = btn.querySelector('img');

    const isHidden = input.type === 'password';
    input.type = isHidden ? 'text' : 'password';

    img.src = isHidden ? '../icons/open_eye.ico' : '../icons/close_eye.ico';
    img.alt = isHidden ? 'Скрыть пароль' : 'Показать пароль';
  });
});