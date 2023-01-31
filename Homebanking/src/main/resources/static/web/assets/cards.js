// let urlParams = new URLSearchParams(window.location.search);
// let urlName = urlParams.get("id");
const app = Vue.

createApp({
    data() {
        return {
            clients: [],
            cards:[],
            debitCard:[],
            creditCard:[]
        }
    },

    created(){
        axios.get("/api/clients/current")
            .then(response => {
                this.allData(response)
            })
            .catch((error) =>{
                console.log(error);
            });
    },
    methods: {
        allData(response){
                this.clients = response.data
                this.cards = this.clients.cards
                this.typeCards()
                
        },
        logOut(){
                    axios.post('/api/logout')
                    .then(location.href = "./index.html")
        },
        newDate(creationDate){ 
            return  new Date(creationDate).toLocaleDateString('es-AR', {month: '2-digit', year: '2-digit'})
        },
        typeCards(){
            this.cards.forEach(type =>{
                if(type.cardType == "CREDIT"){
                    this.creditCard.push(type)
                }
                if(type.cardType == "DEBIT"){
                    this.debitCard.push(type)
                }
            })
        },
        createCards(){
            location.href = "/web/create-cards.html"
        }
    },
}).mount('#app');