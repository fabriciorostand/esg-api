CREATE TABLE ESG_ENDERECO (
                              id_endereco INTEGER PRIMARY KEY,
                              bairro VARCHAR2(100) NOT NULL,
                              rua VARCHAR2(100) NOT NULL,
                              numero VARCHAR2(10) NOT NULL,
                              cep VARCHAR2(8) NOT NULL,
                              cidade VARCHAR2(100) NOT NULL,
                              uf VARCHAR2(2) NOT NULL
);

CREATE TABLE ESG_UNIDADE_CONSUMIDORA (
                                         id_unidade_consumidora INTEGER PRIMARY KEY,
                                         id_endereco INTEGER NOT NULL,
                                         nome VARCHAR2(35) NOT NULL,
                                         tipo VARCHAR2(20) NOT NULL,
                                         area_total NUMBER(10,2) NOT NULL,
                                         CONSTRAINT uc_endereco_fk FOREIGN KEY (id_endereco) REFERENCES ESG_ENDERECO (id_endereco)
);

CREATE TABLE ESG_DISPOSITIVO (
                                 id_dispositivo INTEGER PRIMARY KEY,
                                 id_unidade_consumidora INTEGER NOT NULL,
                                 nome VARCHAR2(100) NOT NULL,
                                 potencia_nominal NUMBER(10,2) NOT NULL,
                                 status VARCHAR2(20) NOT NULL,
                                 consumo_minimo_ativo NUMBER(10,2) NOT NULL,
                                 tempo_ociosidade_limite INTEGER NOT NULL,
                                 CONSTRAINT dispositivo_uc_fk FOREIGN KEY (id_unidade_consumidora) REFERENCES ESG_UNIDADE_CONSUMIDORA (id_unidade_consumidora)
);

CREATE TABLE ESG_SENSOR (
                            id_sensor INTEGER PRIMARY KEY,
                            id_dispositivo INTEGER NOT NULL,
                            ativo CHAR(1) NOT NULL,
                            CONSTRAINT sensor_dispositivo_fk FOREIGN KEY (id_dispositivo) REFERENCES ESG_DISPOSITIVO (id_dispositivo)
);

CREATE TABLE ESG_CONSUMO_ENERGETICO (
                                        id_consumo_energetico INTEGER PRIMARY KEY,
                                        id_sensor INTEGER NOT NULL,
                                        kwh_consumido NUMBER(10,2) NOT NULL,
                                        data_medicao DATE NOT NULL,
                                        CONSTRAINT ce_sensor_fk FOREIGN KEY (id_sensor) REFERENCES ESG_SENSOR (id_sensor)
);

CREATE TABLE ESG_META_CONSUMO (
                                  id_meta_consumo INTEGER PRIMARY KEY,
                                  id_dispositivo INTEGER NOT NULL,
                                  tipo VARCHAR2(3) NOT NULL,
                                  meta_kwh NUMBER(10,2) NOT NULL,
                                  data_inicio DATE NOT NULL,
                                  data_fim DATE NOT NULL,
                                  CONSTRAINT mc_dispositivo_fk FOREIGN KEY (id_dispositivo) REFERENCES ESG_DISPOSITIVO (id_dispositivo)
);

CREATE TABLE ESG_ALERTA_META (
                                 id_alerta_meta INTEGER PRIMARY KEY,
                                 id_meta_consumo INTEGER NOT NULL,
                                 valor_alerta_meta NUMBER(10,2) NOT NULL,
                                 data_alerta_meta DATE NOT NULL,
                                 CONSTRAINT alerta_meta_mc_fk FOREIGN KEY (id_meta_consumo) REFERENCES ESG_META_CONSUMO (id_meta_consumo)
);