package SmartBankAI;

import SmartBankAI.model.customer;
import SmartBankAI.repository.customerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final customerRepository customerRepo;

    @Override
    public void run(String... args) {
        long currentCount = customerRepo.count();
        if (currentCount < 5000) {
            System.out.println(">>> 5000 musteri verisi olusturuluyor, lutfen bekleyin... <<<");

            String[] firstNames = {"Ahmet", "Mehmet", "Mustafa", "Ali", "Huseyin", "Hasan", "Ibrahim", "Ismail", "Osman", "Yusuf",
                    "Fatma", "Ayse", "Emine", "Hatice", "Zeynep", "Elif", "Meryem", "Ozlem", "Seda", "Burcu",
                    "Can", "Deniz", "Cem", "Ece", "Bora", "Eren", "Kaan", "Selin", "Gizem", "Derya"};

            String[] lastNames = {"Yilmaz", "Kaya", "Demir", "Celik", "Sahin", "Yildiz", "Yildirim", "Ozturk", "Aydin", "Ozdemir",
                    "Arslan", "Dogan", "Kilic", "Aslan", "Cetin", "Kara", "Koc", "Kurt", "Ozkan", "Simsek"};

            String[] occupations = {"Yazilim Muhendisi", "Mimar", "Ogretmen", "Doktor", "Avukat", "Muhasebeci", "Hemsire", "Bankaci", "Esnaf", "Pazarlamaci"};

            String[] productOptions = {
                    "Vadesiz TL",
                    "Vadesiz TL, Kredi Karti",
                    "Vadesiz TL, Kredi Karti, Ek Hesap",
                    "Vadesiz TL, Vadeli TL, Yatirim Hesabi",
                    "Vadesiz TL, Ek Hesap"
            };

            Random random = new Random(42); // Tekrarlanabilir gerçekçi dağılım
            List<customer> customers = new ArrayList<>(5000);

            for (int i = 1; i <= 5000; i++) {
                String fullName = firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)];
                String identityNumber = String.valueOf(10000000000L + (long)(random.nextDouble() * 89999999999L));
                int age = 20 + random.nextInt(46); // 20 - 65 yaş
                String occupation = occupations[random.nextInt(occupations.length)];

                // Gerçekçi finansal dağılımlar
                double income = 25000 + (random.nextDouble() * 125000); // 25.000 - 150.000 TL
                double expenseRatio = 0.35 + (random.nextDouble() * 0.45); // Gelirin %35 - %80'i
                double expenses = income * expenseRatio;
                double balance = 2000 + (random.nextDouble() * 200000);
                double debt = random.nextDouble() * 120000;

                int creditScore = 900 + random.nextInt(1001); // 900 - 1900 arası Findeks skoru
                int activeCredits = random.nextInt(5);
                int latePayments = (creditScore < 1200) ? (1 + random.nextInt(4)) : (random.nextDouble() < 0.15 ? 1 : 0);
                int transactions = 3 + random.nextInt(55);
                double ccUsageRatio = BigDecimal.valueOf(0.10 + (random.nextDouble() * 0.85))
                        .setScale(2, RoundingMode.HALF_UP).doubleValue();
                int mobileLogins = 1 + random.nextInt(40);
                String products = productOptions[random.nextInt(productOptions.length)];

                customer c = customer.builder()
                        .fullName(fullName)
                        .identityNumber(identityNumber)
                        .age(age)
                        .occupation(occupation)
                        .monthlyIncome(BigDecimal.valueOf(income).setScale(2, RoundingMode.HALF_UP))
                        .monthlyExpenses(BigDecimal.valueOf(expenses).setScale(2, RoundingMode.HALF_UP))
                        .creditScore(creditScore)
                        .accountBalance(BigDecimal.valueOf(balance).setScale(2, RoundingMode.HALF_UP))
                        .totalDebt(BigDecimal.valueOf(debt).setScale(2, RoundingMode.HALF_UP))
                        .activeCreditsCount(activeCredits)
                        .latePaymentsCount(latePayments)
                        .monthlyTransactionCount(transactions)
                        .creditCardUsageRatio(ccUsageRatio)
                        .mobileAppLoginsPerMonth(mobileLogins)
                        .existingProducts(products)
                        .build();

                customers.add(c);
            }

            // Toplu kayıt (Batch insert)
            customerRepo.saveAll(customers);
            System.out.println(">>> 5000 musteri basariyla veritabanina eklendi! <<<");
        }
    }
}