import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyConverter {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("      CURRENCY CONVERTER");
        System.out.println("=================================");

        System.out.print("Enter Base Currency (USD, INR, EUR, GBP, JPY): ");
        String base = sc.next().toUpperCase();

        System.out.print("Enter Target Currency (USD, INR, EUR, GBP, JPY): ");
        String target = sc.next().toUpperCase();

        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();

        try {

            String url = "https://open.er-api.com/v6/latest/" + base;

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            // Find target currency rate
            Pattern pattern = Pattern.compile("\"" + target + "\":\\s*([0-9.]+)");
            Matcher matcher = pattern.matcher(json);

            if (matcher.find()) {

                double rate = Double.parseDouble(matcher.group(1));
                double convertedAmount = amount * rate;

                System.out.println("\n========== RESULT ==========");
                System.out.println("Base Currency    : " + base);
                System.out.println("Target Currency  : " + target);
                System.out.println("Exchange Rate    : " + rate);
                System.out.printf("Converted Amount : %.2f %s%n",
                        convertedAmount, target);
            } else {
                System.out.println("Invalid Target Currency.");
            }

        } catch (Exception e) {
            System.out.println("Error fetching exchange rate.");
            System.out.println(e.getMessage());
        }

        sc.close();
    }
}