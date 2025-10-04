CREATE TABLE product (
    id BIGSERIAL NOT NULL,
    title VARCHAR(255) NOT NULL,
    handle VARCHAR(255) NOT NULL,
    product_type VARCHAR(255) NOT NULL,
    creation_timestamp TIMESTAMP NOT NULL,
    last_update_timestamp TIMESTAMP NOT NULL,
    primary key (id)
);

CREATE TABLE product_variant (
    id BIGSERIAL NOT NULL,
    product_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    sku VARCHAR(255) NOT NULL,
    price VARCHAR(255) NOT NULL,
    creation_timestamp TIMESTAMP NOT NULL,
    last_update_timestamp TIMESTAMP NOT NULL,
    primary key (id)
);
ALTER TABLE product_variant ADD CONSTRAINT FK_product_id FOREIGN KEY (product_id) REFERENCES product;
