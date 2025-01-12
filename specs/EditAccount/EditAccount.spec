Edit Account
============

| AccountName     |
|-----------------|
| Hesabım         |
| My Wallet       |
| TestAccount     |
     
Account Name Bilgisinin Güncellenmesi
-------------------------------------
tags: smokeTest, regresyon
* Catchylabs login ekraninin acildigi dogrulanir
* Username alanina "oguzhan.alipek" bilgisi girilir
* Password alanina "Oa426336" bilgisi girilir
* Login butonuna tiklanir
* Open Money Transfer butonuna tiklanir
* Account ekraninin acildigi dogrulanir
* Edit Account butonuna tiklanir
* Edit Account Name alanina <AccountName> bilgisi girilir
* Edit Account ekraninda Update butonuna tiklanir
* Account ekraninin acildigi dogrulanir
* Account Name bilgisinin <AccountName> oldugu dogurlanir

Edit Account Ekraninda Account Name Alanı Boş Bırakıldığında Update Butonunun Devre Dışı Olduğu Doğrulanır
----------------------------------------------------------------------------------------------------------
tags: regresyon
* Catchylabs login ekraninin acildigi dogrulanir
* Username alanina "oguzhan.alipek" bilgisi girilir
* Password alanina "Oa426336" bilgisi girilir
* Login butonuna tiklanir
* Open Money Transfer butonuna tiklanir
* Account ekraninin acildigi dogrulanir
* Edit Account butonuna tiklanir
* Edit Account ekraninda Account Name alani temizlenir
* Update butonunun devre disi oldugu dogrulanir