
Pelajaran tentang Thrift
==========================

Sample ini merupakan contoh penggunaan Thrift untuk komunikasi 
RPC diimplementasi dalam bahasa Go dan Scala, dimana Go bertindak
sebagai client dan Scala sebagai server-nya.

Tutorial
-----------

1. Generasikan code untuk Go dan java

```bash
$ thrift --gen go --gen java trancam.thrift
```

2. Buat kode client di Go, lihat `trancam.go`

3. Buat kode server di Scala, lihat `src/example/Trancam.scala`.
4. Kopi direktory `gen-java/gen` ke `src/main/java/`.
5. Jalankan server dengan cara `$ sbt run`.
6. Jalankan client (di terminal lain) dengan cara
```bash
$ go build
$ ./trancam
```

Contoh output kalo berhasil:

``` # client
$ time ./thrift
rv:  250
```

``` # server
[info] Running example.Trancam 
Listening at 7366...
ping received. ts: 2194
```

[] Robin Sy.



