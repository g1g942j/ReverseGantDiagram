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

class Router {
    constructor(){
        this.router = document.getElementById('router');
        if(!this.router) {
            this.router = document.createElement('div');
            this.router.id = 'router';
            document.body.appendChild(this.router);
        }
    }

    async init () {
        await this.checkToken();
    }

    async checkToken() {
        /* localStorage sessionStorage или Cookie??? на выбор Ромчика... */
        const token = localStorage.getItem('authTokenGantHedgehog');
        if(token) {
            /* связь с валидатором... на финал */
            const result = true;
            if(result) {
                window.location.href = './pages/projects.html';
                return;
            }
        }
        /* window.projectsList = []; */

        window.location.href = './pages/auth.html';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    window.router = new Router();
    window.router.init();
});