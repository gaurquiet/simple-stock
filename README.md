# simple-stock
### how to run this
1- calculate dividend yield
GET http://localhost:8080/stocks/POP/dividendYield?price=120
2- Calculate P/E Ratio
GET http://localhost:8080/stocks/POP/peRatio?price=120
3- Record a trade
POST - record trade -http://localhost:8080/stocks/POP/trade
{
"quantity": 100,
"indicator": "buy",
"price": 110.0
}
4- Calculate VWSP
GET http://localhost:8080/stocks/POP/vwsp
5- Calculate GBCE All Share Index
GET http://localhost:8080/stocks/allShareIndex