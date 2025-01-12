# Test Otomasyon Projesi

Bu proje, **Selenium** ve **Gauge (BDD)** kullanılarak geliştirilmiş bir test otomasyon projesidir. **Java** programlama diliyle yazılmış olup, **Maven** proje yapısı kullanılmıştır.

---

## İçindekiler

- [Proje Hakkında](#proje-hakkında)
- [Bağımlılıklar](#bağımlılıklar)
- [Tag Yapılandırması](#tag-yapılandırması)
- [Kurulum](#kurulum)
- [Versiyon Bilgileri](#versiyon-bilgileri)

---

## Proje Hakkında

Bu proje, web tabanlı uygulamalar için otomatik test senaryoları oluşturmayı ve çalıştırmayı amaçlamaktadır. 

### Proje Özellikleri:
- **Selenium WebDriver** kullanılarak web tarayıcıları üzerinde test otomasyonu yapılır.
- **Gauge BDD** frameworkü kullanılarak test senaryoları yazılır.
- **Maven** ile bağımlılıklar yönetilir ve proje yapılandırılır.
- Esnek ve okunabilir bir kod yapısı sunar.

---

## Bağımlılıklar

Projede kullanılan temel bağımlılıklar aşağıdaki gibidir:

```xml
<dependencies>
        <dependency>
            <groupId>com.thoughtworks.gauge</groupId>
            <artifactId>gauge-java</artifactId>
            <version>0.7.3</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>3.141.59</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.8.2</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.17.2</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.17.2</version>
        </dependency>

    </dependencies>
```

Bağımlılıkları eklemek için Maven projenizin `pom.xml` dosyasına yukarıdaki kodu ekleyin.

---

## Tag Yapılandırması

Gauge kullanarak farklı test senaryolarını gruplamak ve yönetmek için etiketleme (tagging) yapısı kullanılabilir. 

Senaryo Başlığı
---------------
tags: smokeTest, regresyon
```
Gauge komutları ile belirli etiketlere sahip senaryolar çalıştırılabilir:
```bash
gauge run specs --tags "smokeTest"
```

---

## Kurulum

Projeyi çalıştırmak için aşağıdaki adımları izleyin:

### Gereksinimler

1. **Java** (JDK 8 veya üstü)
2. **Maven**
3. **Gauge**
4. **IDE**
   
### Çalıştırma Adımları

1. Bu repoyu klonlayın:
   ```bash
   git clone https://github.com/kullanici_adiniz/proje_adi.git
   ```

2. Proje dizinine gidin:
   ```bash
   cd proje_adi
   ```

3. Maven bağımlılıklarını yükleyin:
   ```bash
   mvn clean install
   ```

4. Testleri çalıştırın:
   ```bash
   gauge run specs
   ```

---

## Versiyon Bilgileri

| Araç                | Versiyon       |
|---------------------|----------------|
| Selenium WebDriver  | 3.141.59       |
| Gauge Framework     | 1.0.7          |
| Java                | 8 ve üzeri     |
| Maven               | 3.8.5 ve üzeri |
| Junit5              | 5.8.2          |
| Log4j               | 2.17.2         |

---
