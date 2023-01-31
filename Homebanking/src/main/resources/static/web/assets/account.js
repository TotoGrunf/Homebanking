let urlParams = new URLSearchParams(window.location.search);
let urlName = urlParams.get("id");
const app = Vue.

createApp({
    data() {
        return {
            clients: [],
            accounts:[],
            accountsId:[],
            transactions:[],
            transactionsSort:[],
            debit:0,
            credit:0,
            totalAmount:0,
            debitPercentage:0,
            creditPercentage:0
            
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
                this.accounts = response.data.accounts
                this.accountsId = this.accounts.find(account => account.id == urlName)
                this.transactions = this.accountsId.transactions
                this.transactionsSortId()
                this.amountAccount()
                this.amountsPercentages()
                this.grafic();
        },
        newDate(creationDate){ 
            return  new Date(creationDate).toLocaleDateString()
        },
        newHour(creationDate){ 
            return  new Date(creationDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
        },
        logOut(){
            axios.post('/api/logout')
            .then(location.href = "./index.html")
        },
        grafic(){
            let ctx = document.getElementById("miChart").getContext("2d");
            let myChart = new Chart(ctx,{
                type:"pie",
                data:{
                    labels:['Debit' + ' ' + this.debitPercentage +'%' ,'Credit'+ ' ' + this.creditPercentage +'%'],
                    datasets:[{
                        label:'Num datos',
                        data:[this.debit,this.credit],
                        backgroundColor:[
                            'rgb(255, 0, 0)',
                            'rgb(74, 135, 72)'
                        ]
                    }]
                }
            })
        },
        transactionsSortId(){
            this.transactionsSort = this.transactions.sort((a, b) =>{
                if(a.id < b.id){
                    return 1
                }
                else if(a.id > b.id){
                    return -1
                }
                else{
                    return 0
                }
            })
        },
        amountAccount(){
            this.transactions.forEach(num => {
                if(num.type == 'DEBIT'){
                    this.debit += num.amount
                }
            });
            this.transactions.forEach(num => {
                if(num.type == 'CREDIT'){
                    this.credit += num.amount
                }
            });
        },
        amountsPercentages(){
                this.totalAmount =  -this.debit + this.credit
                this.debitPercentage = (this.debit * -100) / this.totalAmount
                this.creditPercentage = (this.credit * 100) / this.totalAmount
                this.debitPercentage = Math.round(this.debitPercentage)
                this.creditPercentage = Math.round(this.creditPercentage)
        }
    },
}).mount('#app');