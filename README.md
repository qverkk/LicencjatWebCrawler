# LicencjatWebCrawler

This is an application designed for my diploma. It is designed to represent Java threads and Kotlin coroutines. To do so, i've made a multi-threaded web crawler that has customizable settings.

Current settings:
1. Choice to use threads or coroutines
2. Number of threads/coroutines to use
3. The depth of search for maximum websites

I've also included loading REST API data from a website with loading the data on the current thread, or a new thread, to represent the benefits of loading data on a seprate thread.

The results in this application include:
- A binary hierarchy of found websites in the process
- Time it took to find all of the websites
