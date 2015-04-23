server {
    bank.url = "http://localhost:8080/"
    egov.url = "http://localhost:8081/"
}

environments {
    dev {
        server {
            bank.url = "http://devhost:port/"
            egov.url = "http://devhost:port/"
        }
    }
}