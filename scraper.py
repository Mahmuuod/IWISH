import requests
from bs4 import BeautifulSoup
import oracledb

'''# Database connection details
dsn = oracledb.makedsn('hostname', 'port', service_name='service_name')
connection = oracledb.connect(user='your_username', password='your_password', dsn=dsn)
cursor = connection.cursor()
'''
# Function to scrape product data
def scrape_product_data(url):
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    }
    response = requests.get(url, headers=headers)
    soup = BeautifulSoup(response.content, 'html.parser')


    print(soup.select_one("#product-price-42393 > span"))
    category = soup.select_one("body > div.wrapper > div > div.top-container > div > div > div > div > ul > li.category126 > strong").text
    name = soup.select_one("body > div.wrapper > div > div.main-container.col2-left-layout > div > div.row > div.col-main.col-sm-9.f-right > div.category-products > ul > li.item.nth-child-2np1.nth-child-3np1.nth-child-4np1.nth-child-5np1.nth-child-6np1.nth-child-7np1.nth-child-8np1 > div > div.details-area > h2 > a").text 
    price = float(soup.select_one("#product-price-42393 > span").text.strip().replace('EGP', '').replace(',', '')) 
      

    return {
        'name': name,
        'price': price,
        'category': category
    }

# Function to insert data into the database
def insert_into_database(item_id, name, price, category):
    sql = """
    INSERT INTO Item (Item_id, Name, Price, Category)
    VALUES (:1, :2, :3, :4)
    """
    cursor.execute(sql, (item_id, name, price, category))
    connection.commit()


if __name__ == "__main__":
    product_url = "https://www.marketchino.com/en/categories/winter-collection.html" 
    product_data = scrape_product_data(product_url)
    print(product_data['name'], product_data['price'], product_data['category'])
    
    '''item_id = 1  # Replace with a unique ID

    # Insert data into the database
    insert_into_database(
        item_id=item_id,
        name=product_data['name'],
        price=product_data['price'],
        category=product_data['category']
    )

    print("Data inserted successfully!")

    # Close the database connection
    cursor.close()
    connection.close()'''