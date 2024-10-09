
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Currency Selection: Allow the user to choose the base currency and the target currency
        System.out.println("Enter the base currency (e.g. USD, EUR, JPY): ");
        String baseCurrency = scanner.next().toUpperCase();

        System.out.println("Enter the target currency (e.g. USD, EUR, JPY): ");
        String targetCurrency = scanner.next().toUpperCase();

        // Amount Input: Take input from the user for the amount they want to convert
        System.out.println("Enter the amount to convert: ");
        double amount = scanner.nextDouble();

        // Currency Rates: Fetch real-time exchange rates from a reliable API
        double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);

        // Currency Conversion: Convert the input amount from the base currency to the target currency
        double convertedAmount = amount * exchangeRate;

        // Display Result: Show the converted amount and the target currency symbol to the user
        System.out.printf(" Converted amount: %.2f %s%n", convertedAmount, getCurrencySymbol(targetCurrency));
    }

    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + baseCurrency))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            // Parse the JSON response to extract the exchange rate
            // For simplicity, assume the API response is in the format: {"rates":{"TARGET_CURRENCY":EXCHANGE_RATE}}
            int startIndex = responseBody.indexOf(targetCurrency) + targetCurrency.length() + 3;
            int endIndex = responseBody.indexOf("}", startIndex);
            String exchangeRateStr = responseBody.substring(startIndex, endIndex);
            return Double.parseDouble(exchangeRateStr);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching exchange rate: " + e.getMessage());
            return 0.0;
        }
    }

    private static String getCurrencySymbol(String currencyCode) {
        // For simplicity, use a hardcoded map of currency codes to symbols
        // In a real-world application, you would use a more robust approach
        switch (currencyCode) {
            case "USD":
                return "$";
            case "EUR":
                return "€";
            case "JPY":
                return "¥";
            default:
                return "";
        }
    }
}

