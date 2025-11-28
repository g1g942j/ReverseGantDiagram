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

const form = document.getElementById("passwordResetForm");
const email = document.getElementById("email");
const loginButton = document.getElementById("loginButton");

form.addEventListener("submit", (e) => {
  e.preventDefault();
  const value = email.value.trim();

  if (!value) {
    alert("Введите email.");
    email.focus();
    return;
  }

  const submitBtn = form.querySelector(".btn.primary");
  submitBtn.disabled = true;

  /* принимать ответ от бека насчёт валидации или провести напрямую в фронте???
  ответ от сброса пароля беком принимать не надо...
  */

  setTimeout(() => {
    alert("Если такой email существует, мы отправили письмо со ссылкой для сброса пароля.");
    submitBtn.disabled = false;
  }, 600);
});

loginButton.addEventListener("click", () => {
  window.location.href = "./auth.html";
});