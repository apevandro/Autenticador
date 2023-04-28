# Introdução

Implementação do algoritmo TOTP – Time-based One Time Password para geração de token.

1. Recebimento de uma hash/key (via texto)
2. O client (desktop) receberá a hash como entrada
3. A partir dessa hash, o cliente gerará um token de seis dígitos numéricos
4. A cada 30 segundos um novo token deverá ser gerado. O client exibirá esse novo token


# Getting Started

TODO: Guide users through getting your code up and running on their own system. In this section you can talk about:
1. Installation process
2. Software dependencies
3. Latest releases
4. API references


# Build and Test


``` Linux BASH ```

mvnw clean test    (run unit tests)

mvnw clean test -Dtest=TotpServiceImplTest    (run single test)

mvnw clean verify -DskipTests    (skip unit tests and build a JAR file)

mvnw clean verify    (run unit tests and build a JAR file)

```


``` Windows Environment ```

mvnw.cmd clean test    (run unit tests)

mvnw.cmd clean test -Dtest=TotpServiceImplTest    (run single test)

mvnw.cmd clean verify -DskipTests    (skip unit tests and build a JAR file)

mvnw.cmd clean verify    (run unit tests and build a JAR file)

```

# Run

```
java -jar autenticador-1.0.0.jar
```


# Environment and Tools

JavaFX Scene Builder v8.5.0

Eclipse Java EE IDE for Web Developers. Version: Photon Release (4.8.0)

Eclipse e(fx)clipse extension v3.3.0

Java SE Development Kit 8u221


### Implementação Java Utilizada

* [TOTP library for Java](https://github.com/jchambers/java-otp)


### Secrets da base Oracle para utilização com o algoritmo TOTP

TL6LJWAO2VKQQSG6, UVBOPESJ77HJQEE5, JHQL5YJYJXIYR4QO, LAT3DJPVVAURVGXH, P3Q2DFHCA5GNHDTB

CZDNKTVLREWI2TER, NVV3TYQZRBYTOIF7, 7RB3LCPSOUY5WUAK, KWI2HWRQW2XGIEVW, 3IQSSUQTJBFFSGEF

5RFFKIAWTN2TS3I3, FNX2GV6SSDGC5FC3, IUW4IXBLAFXAUZIR, 4G5J5N3GIMQ7RV72, 7TU5EA2I2AEFWXH5

JT26FV2IKKQLAZ25, YSP627LKTHY57VE6, SLERZFT6LZHYHMHM, ROWEB2L6Y5RAZNMZ, DURNALBZEUL5NEAR

HNJG4UH6S55UAHWN, 6VXYCZ7EUY767LE6, BW6MGCPHUHHNGHZA, FGOYG6OFAEAQBKUK, 2DAAM65RZFBUFOZV

H7ZOKWFU6A5LENSC, HZGTUOTORK2I3ZSQ, SN5DZSVKWUQTQ3Q5, OSFBWDJUHI7RZKDB, N2UURLUBLPPGQB7J


### Referências

Para referências adicionais, por favor, considere as seguintes seções

* [Wiki page](http://wiki2.spcbr.int.br/index.php/SPC_Autenticador_-_Desktop)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/2.1.7.RELEASE/reference/html/)
* [TOTP Token Generator](https://totp.danhersam.com/)
* [Time-based One-time Password Algorithm](https://rosettacode.org/wiki/Time-based_One-time_Password_Algorithm)