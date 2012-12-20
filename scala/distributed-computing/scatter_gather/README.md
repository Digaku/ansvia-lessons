Scatter Gather
===============

Ini adalah contoh implementasi pattern Scatter-Gather yang biasanya digunakan dalam distributed computing.


![Scatter Gather](http://www.eaipatterns.com/img/BroadcastAggregate.gif)


Sampel output
---------------

	23:11:47.365 [default-akka.actor.default-dispatcher-5] DEBUG c.a.belajar.scattergather.Agregator - Processing: Hello
	23:11:47.384 [default-akka.actor.default-dispatcher-5] DEBUG c.a.belajar.scattergather.Agregator - binding result...
	result 1: [5]: Hello[4]: Hello[3]: Hello[2]: Hello[1]: Hello
	23:11:47.395 [default-akka.actor.default-dispatcher-5] DEBUG c.a.belajar.scattergather.Agregator - Processing: Robin
	23:11:47.397 [default-akka.actor.default-dispatcher-5] DEBUG c.a.belajar.scattergather.Agregator - binding result...
	result 2: [5]: Robin[4]: Robin[3]: Robin[2]: Robin[1]: Robin

Referensi: http://blog.vasilrem.com/scatter-gather-with-akka-dataflow

