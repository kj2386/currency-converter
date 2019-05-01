## Currency Converter

**Project description:** Currency converter using fixer.io currency API to get current currency exchange rates every 5 hours and saving it onto a Redis database. This task is automatic in the background. From the main page, you can convert between 2 currencies and the result will be rounded to two decimal places. Used Spring Boot and Spring MVC.  For the complete project code, see it on Github [here](https://github.com/kj2386/currency-converter).

### Redis configuration
```java
@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }
}
```

### Redis task to update database every 5 hours with current exchange rates
```java
@Component
public class CurrencyTask {

    @Autowired
    private CurrencyRepository currencyRepository;

    //runs every 5 hours
    @Scheduled(fixedRate = 5 * 1000 * 60 * 60)
    private void getRatesTask() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            CurrencyDTO forObject = restTemplate.getForObject("http://data.fixer.io/api/latest?access_key=85b152dd1a19ee1bb3fb8bfef246416c", CurrencyDTO.class);
            forObject.getRates().forEach((key, value) -> {
                Currency currency = new Currency(key, value);
                this.currencyRepository.save(currency);
            });
        } catch (RestClientException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
```
### Get currencies and convert them
```java
 public Optional<Double> convert(ConversionCurrency conversionCurrency) {
       Optional<Currency> toOptional = this.currencyRepository.findById(conversionCurrency.getTo());
       Optional<Currency> fromOptional = this.currencyRepository.findById(conversionCurrency.getFrom());

       if (toOptional.isPresent() && fromOptional.isPresent()) {
           Currency to = toOptional.get();
           Currency from = fromOptional.get();
           Double toValue = to.getValueInEuros();
           Double fromValue = from.getValueInEuros();

           Double answer = toValue * conversionCurrency.getValue() / fromValue;
           Double result = Math.round(answer * 100.0) / 100.0;

           return Optional.of(result);
       }


       return Optional.empty();

    }
```
### Main Page Controller
```java
@Controller
public class WebPageController {

    @Autowired
    CurrencyService currencyService;

    NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @GetMapping("/home")
    public String showWebPage(Model theModel) {


        ConversionCurrency conversionCurrency = new ConversionCurrency();
        theModel.addAttribute("conversion", conversionCurrency);

        List<Currency> from = currencyService.getAllCurrencies();
        theModel.addAttribute("fromCurrency", from);

        List<Currency> to = currencyService.getAllCurrencies();
        theModel.addAttribute("toCurrency", to);


        return "currency-converter";
    }

    @PostMapping("/convert")
    public String showResult(@ModelAttribute("conversion") ConversionCurrency conversionCurrency, Model theModel) {

        List<Currency> from = currencyService.getAllCurrencies();
        theModel.addAttribute("fromCurrency", from);

        List<Currency> to = currencyService.getAllCurrencies();
        theModel.addAttribute("toCurrency", to);


        Optional<Double> answer = currencyService.convert(conversionCurrency);

        if (answer.isPresent()) {

           conversionCurrency.setResult(answer.get());
            return "currency-converter";
        }

        return "currency-converter";
    }

}
```
### View page using html, css, bootstrap, and thymeleaf
```java
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.com">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

    <title>Currency Converter</title>
</head>
<body>

<div class="jumbotron">
    <div class="container">
        <h1 class="display-3">Currency Converter</h1>
    </div>
</div>

<form action="#" th:action="@{/convert}" th:object="${conversion}" method="POST">

    <div class="container">
        <div class="row">
            <div class="col-md-2">
                <span>From: </span>
                <div class="dropdown" style="width 150px">
                    <select th:field="*{from}" class="btn btn-secondary dropdown-toggle" type="button"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <option th:value="0">Choose:</option>
                        <option th:each="currency : ${fromCurrency}" th:value="${currency.name}"
                                th:text="${currency.name}"></option>
                    </select>
                </div>
                <span>To: </span>
                <div class="dropdown" style="width 150px">
                    <select th:field="*{to}" class="btn btn-secondary dropdown-toggle" type="button"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <option th:value="0">Choose:</option>
                        <option th:each="currency : ${toCurrency}" th:value="${currency.name}"
                                th:text="${currency.name}"></option>

                    </select>
                </div>
            </div>
            <div class="col-md-4">
                <br>
                <div>
                    <input type="number" step=".01" th:field="*{value}" placeholder="Amount" required/>
                </div>
                <br>
                <div>
                    <input class="btn btn-success" name="submit" type="submit" value="Convert"/>
                </div>
            </div>
            <div class="col-md-4">
                <h2>Result:</h2>

                <h3><span th:text="*{result}"></span></h3>
            </div>


        </div>

    </div>

</form>


</body>
</html>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
```
### Final Result
[<img src="images/currencyconverter.png?raw=true"/>](https://raw.githubusercontent.com/kj2386/currency-converter/master/images/currencyconverter.png)
   
