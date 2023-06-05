package com.example.ics;

import com.microsoft.playwright.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPlaywright {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate("http://localhost:4200/classify");

            Thread.sleep(250);

            page.click(".clr-combobox-wrapper .clr-combobox-trigger");
            page.click("clr-option[clrvalue=\"clarifai\"]");

            Thread.sleep(250);

            page.fill("input[formcontrolname=\"imageUrl\"]", "https://th.bing.com/th/id/R.fd6e965e5f9050f3b9060f953647f779?rik=rp1%2bUMaSp3sI5Q&pid=ImgRaw&r=0");

            Thread.sleep(250);

            page.click("button.btn-primary");

            String firstTag = page.textContent("table.table tbody tr:first-child td:nth-child(2)");
            String initialURL = page.url();
            System.out.println("First Tag: " + firstTag);

            Thread.sleep(250);

            page.click("a[routerlink=\"/gallery\"]");
            page.fill("input#Tag", "car");
            Thread.sleep(250);

            page.click("button.ics-sort");

            Thread.sleep(250);
            page.click(".card.clickable");
            String currentFirstTag = page.textContent("table.table tbody tr:first-child td:nth-child(2)");
            assertEquals(firstTag, currentFirstTag);

            boolean isFirstTagSame = firstTag.equals(currentFirstTag);
            page.click("button.btn.btn-primary");
            String finalURL = page.url();

            assertEquals(initialURL, finalURL);

            browser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
