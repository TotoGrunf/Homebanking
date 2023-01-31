const app = Vue.

createApp({
    data() {
        return {
            client: [],
            accounts:[],
            loans:[],
            accountType:"",
            accountId:"",
            totalAccountsBalance:0,
            totalLoansBalance:0,
            totalBalance:0,
            accountsBalancePercentage:0,
            paymentsBalancePercentage:0
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
        newAccount(){
            axios.post("/api/clients/current/accounts",`accountType=${this.accountType}`,
                {headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(
                    Swal.fire(
                        'Good job!',
                        'You account has been created!',
                        'success',
                        setTimeout(() => { location.href = "/web/accounts.html" }, 1500)
                    )
                )

                .catch(error =>
                    Swal.fire({
                        icon: 'error',
                        title: 'You cannot create more accounts (MAX-3)',
                    }),
                    setTimeout(() => { location.href = "/web/accounts.html" }, 1000)
                )
        },
        allData(response){
                
                this.client = response.data
                this.accounts = response.data.accounts
                this.accounts = this.accounts.filter(status => status.active == true)
                this.loans = response.data.clientLoans
                this.balanceAccounts()
                this.balancePercentages()
                this.grafic()
                console.log(response);
        },
        newDate(creationDate){
           return creationDate = new Date(creationDate).toLocaleDateString()
        },
        logOut(){
            axios.post('/api/logout')
            .then(location.href = "./index.html")
        },
        grafic(){
            let ctx = document.getElementById("miChart").getContext("2d");
            let myChart = new Chart(ctx,{
                type:"doughnut",
                data:{
                    labels:['Ac balance' + ' ' +this.accountsBalancePercentage + '%','Payments' + ' ' + this.paymentsBalancePercentage + '%'],
                    datasets:[{
                        label:'Num datos',
                        data:[this.totalAccountsBalance,this.totalLoansBalance],
                        backgroundColor:[
                            'rgb(66, 134, 244)',
                            'rgb(74, 135, 72)'
                        ]
                    }]
                }
            })
        },
        balanceAccounts(){
            this.accounts.forEach(account => {
                this.totalAccountsBalance += account.balance
            });
            this.loans.forEach(loan =>{
                this.totalLoansBalance += loan.amount
            })
            
        },
        balancePercentages(){
            this.totalBalance = this.totalAccountsBalance + this.totalLoansBalance
            this.accountsBalancePercentage = (this.totalAccountsBalance * 100) / this.totalBalance
            this.paymentsBalancePercentage = (this.totalLoansBalance * 100) / this.totalBalance
            this.accountsBalancePercentage = Math.round(this.accountsBalancePercentage)
            this.paymentsBalancePercentage = Math.round(this.paymentsBalancePercentage)
        },
        removeAccount(){
            axios.patch('/api/clients/current/accounts/remove',`accountId=${this.accountId}`)
            .then(() => {
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: "your account is delete",
                    showConfirmButton: false,
                    timer:1500
                }),
                setTimeout(() => { location.href = "/web/accounts.html"}, 1500)
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    timer:2000,
                    showConfirmButton: false,
                    text:error.response.data
                })
            });
        }
    },
}).mount('#app');