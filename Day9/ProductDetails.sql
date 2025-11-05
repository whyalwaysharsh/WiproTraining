/*
Question 2: Product Details
---------------------------
Display products under the category furniture, laptop, or mobile, 
and with price less than 50000.
*/

SELECT 
    *
FROM 
    product
WHERE 
    category IN ('furniture', 'laptop', 'mobile')
    AND price < 50000;
