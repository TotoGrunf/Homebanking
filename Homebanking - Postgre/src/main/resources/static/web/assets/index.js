const app = Vue.
    createApp({
        data() {
            return {
                email1: "",
                pwd1: "",
                
                firstName: "",
                lastName: "",
            }
        },
        created() {

        },
        methods: {
            login() {
                axios.post("/api/login", `email=${this.email1}&pwd=${this.pwd1}`,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(response => location.href = "/web/accounts.html")
                    .catch((error) => {
                        Swal.fire({
                            icon: 'error',
                            text: 'Something went wrong!'
                        })
                    });
            },
            register() {
                axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email1}&password=${this.pwd1}`,
                    { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    
                    .then(response => this.login())
            },
            loginButton(){
                container.classList.remove("right-panel-active");
            },
            registerButton(){
                container.classList.add("right-panel-active");

            }

        },
    }).mount('#app');