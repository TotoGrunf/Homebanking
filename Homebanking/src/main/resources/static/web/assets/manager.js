const app = Vue.

createApp({
    data() {
        return {
            clients: [],
            firstName:"",
            lastName:"",
            email:"",
        }
    },

    created(){
        axios.get("/api/clients")
            .then(response => {
                this.clients = response.data;
            })
            .catch((error) =>{
                console.log(error);
            });
    },
    methods: {
        addClient(){
            if(this.firstName != "" && this.lastName != "" && this.email != ""){
                axios.post("/rest/clients", {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email,
                })
                .then(() => window.location.reload())
                .catch(function (error) {
                    console.log(error);
                })
            }
        }
    },
}).mount('#app');