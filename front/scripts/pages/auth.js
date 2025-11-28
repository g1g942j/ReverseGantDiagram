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

const password = document.getElementById("password");
const togglePassword = document.getElementById("togglePassword");

togglePassword.addEventListener("click", () => {
  const input = password;
  const img = togglePassword.querySelector("img");
  const hidden = input.type === "password";

  input.type = hidden ? "text" : "password";
  img.src = hidden ? "../icons/open_eye.ico" : "../icons/close_eye.ico";
  img.alt = hidden ? "Скрыть пароль" : "Показать пароль";
});

const form = document.getElementById("authForm");
const email = document.getElementById("email");
const btn = form.querySelector(".btn.primary");

form.addEventListener("submit", (e) => {
  e.preventDefault();
  if (!email.value.trim() || !password.value.trim()) {
    alert("Пожалуйста, заполните email и пароль.");
    return;
  }

  const authRequest = {
    email: email.value.trim(),
    password: password.value,
  };

  const result = true;
  /* отправка на бек, авторизация, загрузка списка проектов
  (задачи наверно будем загружать при открытии страницы проекта)
  */

  btn.disabled = true;

  setTimeout(() => {
    alert("Успешная авторизация");
    btn.disabled = false;
    window.location.href = "../pages/projects.html";
  }, 600);
});