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
        /* localStorage sessionStorage или Cookie??? на выбор бекендера... */
        const token = localStorage.getItem('authTokenGantHedgehog');
        if(token) {
            /* связь с валидатором... на финал */
            const result = true;
            if(result) {
                await this.loadPage('projects');
                return;
            }
        }
        await this.loadPage('registration');
        window.projectsList = [];
    }
    
    async loadPage(page) {
        try {
            const html = await (await fetch(`/front/pages/${page}.html`)).text();
            this.router.innerHTML = html;
            await this.loadScript(page);
        } catch(error) {
            console.error(`Ошибка загрузки страницы ${page}:`, error);
        }
    }

    async loadScript(page) {
        return new Promise(resolve => {
            try {
                const script = document.createElement('script');
                script.src = `/front/scripts/pages/${page}.js`;
                script.onload = () => {
                    resolve();
                };
                document.body.appendChild(script);
            } catch(error) {
                console.error(`Ошибка загрузки скрипта ${page}:`, error);
            }
        });
    }
}


document.addEventListener('DOMContentLoaded', () => {
    window.router = new Router();
    window.router.init();
});