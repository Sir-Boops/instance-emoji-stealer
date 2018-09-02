# Instance emoji stealer
It can't get much simpler

How to

* Clone this repo and enter that dir

* run ./gradlew build ( gradlew.bat build on windows )

* The jarfile is located in build/libs/

* Get the domain you want all the emojis from example 'mastodon.sergal.org'

* Run the jar passing the domain example java -jar instance-emoji-stealer.jar mastodon.sergal.org That will have generated a folder full of emoji pngs and a tar.gz file if you want to steal all the emojis from said instance just take the created tar file else remove any emojis you don't want

* Assuming you want all the emojos copy the created tar.gz file to your mastodon server and run tootctl emoji import targzfilenamehere.tar.gz and all those emojis are yours!
