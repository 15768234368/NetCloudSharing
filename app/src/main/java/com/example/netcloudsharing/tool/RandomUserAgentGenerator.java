package com.example.netcloudsharing.tool;

import java.util.Random;

public class RandomUserAgentGenerator {
    private static final String[] USER_AGENTS = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36",
            // 添加更多的User-Agent字符串...
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:89.0) Gecko/20100101 Firefox/89.0",
            "Mozilla/5.0 (Linux; Android 11; SM-G975F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (Linux; Android 10; SM-A505F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36 OPR/65.3.3216.58675",
            "Mozilla/5.0 (X11; CrOS x86_64 8172.45.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36 Edg/90.0.818.66",
            "Mozilla/5.0 (Linux; Android 11; M2101K6G) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Mobile Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_4_0) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1.1 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:78.0) like Gecko",
            "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:90.0) Gecko/20100101 Firefox/90.0",
            "Mozilla/5.0 (Linux; Android 12; Pixel 6 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Mobile Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (Linux; Android 11; SM-G991U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Mobile Safari/537.36",
            "Mozilla/5.0 (X11; CrOS x86_64 8172.62.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36 Edg/92.0.902.73",
            "Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Mobile Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:100.0) Gecko/20100101 Firefox/100.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_16_0) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:79.0) like Gecko",
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0",
            "Mozilla/5.0 (Linux; Android 11; SM-N986U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Mobile Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 15_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (Linux; Android 11; M2102J20SG) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Mobile Safari/537.36",
            "Mozilla/5.0 (X11; CrOS x86_64 8172.56.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.85 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36 Edg/93.0.961.52",
            "Mozilla/5.0 (Linux; Android 12; SM-G998U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Mobile Safari/537.36",
    };

    public static String getRandomUserAgent() {
        Random random = new Random();
        int index = random.nextInt(USER_AGENTS.length);
        return USER_AGENTS[index];
    }
}