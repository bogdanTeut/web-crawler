Web Crawler

FEW CONSIDERATIONS:
* There are lots of topics to consider here. I tried to touch all of them to minimum degree.
* I tried to separate each of these topics in a separate component so they are visible, easy to read and interchangeable.
* I used TDD for implementing all these features.
* I used cobertura plugin to generate reports after tests are ran about
* the code coverage. mvn cobertura:cobertura.
* The code, the tests are far from being done in the sense of not being polished. E.g. in the tests I could have created
* a generic method to inject Parser mock and reuse it every where, but because of time constraints I didn't do it.

1. Algorithm to crawl is a Breadth First Search. Using a LinkedList preserve the order.
2. Normalization of the URLS.
It's a very complex topic which involves taking into consideration all the the aspects of the URL.
From simplicity reason I reduced it only to using lowercase.
3. Validation of the urls. I chose to use apache library for that. Simplest way, giving the time constraints.
4 Parsing. I used jsoup library. Again, the work I've done was minimal.
5 I am using a regex to obtain the domain and the file extensions (when we are dealing with images for example).
I am doing the expensive operation (Pattern.compile) as a constant field and then use it for fetching the domain and extension.

THINGS TO BE DONE:
1. Polishing and refactoring.
2 I would have added an integration test where a server would've been deployed with a basic website (few pages)
and then test the functionality and this way covering all the case scenarios...

RUNNING THE APP:
mvn exec:java -Dexec.args (we are using the exec-maven-plugin)
or running the main aplication.

