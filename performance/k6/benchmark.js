import http from 'k6/http';
import {check, fail, sleep} from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://product-api:8080';

const JSON_HEADERS = {
    headers: {
        'Content-Type': 'application/json',
    },
};

export const options = {
    scenarios: {
        product_creation: {
            executor: 'shared-iterations',
            exec: 'createProduct',
            vus: 25,
            iterations: 1000,
            maxDuration: '2m',
        },

        price_by_date: {
            executor: 'shared-iterations',
            exec: 'getPriceByDate',
            vus: 50,
            iterations: 20000,
            maxDuration: '5m',
        },

        price_history: {
            executor: 'shared-iterations',
            exec: 'getPriceHistory',
            vus: 50,
            iterations: 15000,
            maxDuration: '5m',
        },
    },

    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500', 'p(99)<1000'],
        checks: ['rate>0.99'],
    },
};

export function setup() {
    waitForApi();

    const productResponse = http.post(
        `${BASE_URL}/products`,
        JSON.stringify({
            name: 'Benchmark product',
            description: 'Product used for performance testing',
        }),
        JSON_HEADERS,
    );

    if (productResponse.status !== 201) {
        fail(
            `Could not create the product: ` +
            `${productResponse.status} ${productResponse.body}`,
        );
    }

    const productId = productResponse.json('id');

    if (!productId) {
        fail(`The response does not contain an ID: ${productResponse.body}`);
    }

    const priceResponse = http.post(
        `${BASE_URL}/products/${productId}/prices`,
        JSON.stringify({
            value: 99.99,
            initDate: '2024-01-01',
            endDate: '2024-12-31',
        }),
        JSON_HEADERS,
    );

    if (priceResponse.status !== 201) {
        fail(
            `Could not add the price: ` +
            `${priceResponse.status} ${priceResponse.body}`,
        );
    }

    return {productId};
}

export function createProduct() {
    const response = http.post(
        `${BASE_URL}/products`,
        JSON.stringify({
            name: `Benchmark product ${__VU}-${__ITER}`,
            description: `Created by virtual user ${__VU}`,
        }),
        {
            ...JSON_HEADERS,
            tags: {
                endpoint: 'product_creation',
            },
        },
    );

    check(response, {
        'product creation returns 201': (res) => res.status === 201,
        'created product contains an ID': (res) => res.json('id') !== null,
    });
}

export function getPriceByDate(data) {
    const response = http.get(
        `${BASE_URL}/products/${data.productId}/price?date=2024-04-15`,
        {
            tags: {
                endpoint: 'price_by_date',
            },
        },
    );

    check(response, {
        'price by date returns 200': (res) => res.status === 200,
        'price value is correct': (res) =>
            Number(res.json('value')) === 99.99,
    });
}

export function getPriceHistory(data) {
    const response = http.get(
        `${BASE_URL}/products/${data.productId}/prices`,
        {
            tags: {
                endpoint: 'price_history',
            },
        },
    );

    check(response, {
        'price history returns 200': (res) => res.status === 200,
        'price history contains one price': (res) => {
            const prices = res.json('prices');
            return Array.isArray(prices) && prices.length === 1;
        },
    });
}

function waitForApi() {
    for (let attempt = 1; attempt <= 60; attempt++) {
        const response = http.get(`${BASE_URL}/actuator/health`);

        if (
            response.status === 200 &&
            response.json('status') === 'UP'
        ) {
            console.log('The API is available');
            return;
        }

        console.log(`Waiting for the API: attempt ${attempt}/60`);
        sleep(2);
    }

    fail('The API did not start within 120 seconds');
}
