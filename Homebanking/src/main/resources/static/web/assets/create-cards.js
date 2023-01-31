const app = Vue.

createApp({
    data() {
        return {
            clients: [],
            cards:[],
            debitCard:[],
            creditCard:[],
            cardType:[],
            cardColor:[]
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
        newCard(){
            axios.post('/api/clients/current/cards', `cardColor=${this.cardColor}&cardType=${this.cardType}`,
                    {headers: {'content-type': 'application/x-www-form-urlencoded'}})
                    .then(response => location.href = "/web/cards.html")
                    .catch((error) => {
                        Swal.fire({
                            icon: 'error',
                            text: 'the email already exists'
                        })
                    });
        },
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
        }
    },
}).mount('#app');