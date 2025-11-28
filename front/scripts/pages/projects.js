window.projectsList = [
  {
    title: "Разработка реверсивной диаграммы Ганта",
    tasksHref: "#",
    usersHref: "#"
  },
  {
    title: "LLMs",
    tasksHref: "#",
    usersHref: "#"
  }
];

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

const projectsContainer = document.getElementById("projectsContainer");

function renderProjects() {
  if (!projectsContainer) return;

  const list = Array.isArray(window.projectsList) ? window.projectsList : [];

  projectsContainer.innerHTML = "";

  if (!list.length) {
    const empty = document.createElement("p");
    empty.textContent = "Проекты не найдены.";
    projectsContainer.appendChild(empty);
    return;
  }

  list.forEach(project => {
    const {
      title = "Без названия",
      tasksHref = "#",
      usersHref = "#"
    } = project || {};

    const card = document.createElement("article");
    card.className = "project-card";
    card.dataset.title = title.toLowerCase();

    const body = document.createElement("div");
    body.className = "project-card-body";

    const h2 = document.createElement("h2");
    h2.className = "project-card-title";
    h2.textContent = title;

    const actions = document.createElement("div");
    actions.className = "project-card-actions";

    const tasksLink = document.createElement("a");
    tasksLink.href = tasksHref;
    tasksLink.className = "btn small";
    tasksLink.textContent = "Задачи";

    const usersLink = document.createElement("a");
    usersLink.href = usersHref;
    usersLink.className = "btn small";
    usersLink.textContent = "Пользователи";

    actions.appendChild(tasksLink);
    actions.appendChild(usersLink);

    body.appendChild(h2);
    body.appendChild(actions);

    card.appendChild(body);

    projectsContainer.appendChild(card);
  });
}

renderProjects();

const searchInput = document.getElementById("projectSearch");

searchInput.addEventListener("input", () => {
  const query = searchInput.value.trim().toLowerCase();
  const cards = projectsContainer.querySelectorAll(".project-card");

  cards.forEach(card => {
    const title = card.dataset.title || "";
    card.style.display = title.includes(query) ? "" : "none";
  });
});