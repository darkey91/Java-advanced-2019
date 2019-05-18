<td id="main"><h3 id="homework-1">Домашнее задание 1. Обход файлов</h3><ol><li>
            Разработайте класс <tt>Walk</tt>, осуществляющий подсчет хеш-сумм файлов.
            <ol><li>
                    Формат запуска
                    <pre>java Walk &lt;входной файл&gt; &lt;выходной файл&gt;</pre></li><li>
                    Входной файл содержит список файлов, которые требуется обойти.
                </li><li>
                    Выходной файл должен содержать по одной строке для каждого
                    файла. Формат строки:
                    <pre>&lt;шестнадцатеричная хеш-сумма&gt; &lt;путь к файлу&gt;</pre></li><li>
                    Для подсчета хеш-суммы используйте алгоритм
                    <a href="https://ru.wikipedia.org/wiki/FNV">FNV</a>.
                </li><li>
                    Если при чтении файла возникают ошибки, укажите в качестве
                    его хеш-суммы <tt>00000000</tt>.
                </li><li>
                    Кодировка входного и выходного файлов — UTF-8.
                </li><li>
                	Если родительская директория выходного файла
                	не существует, то соответствующий путь надо создать.
                </li><li>
                    Размеры файлов могут превышать размер оперативной памяти.
                </li><li><p>Пример</p><p>Входной файл</p><pre>                        java/info/kgeorgiy/java/advanced/walk/samples/1
                        java/info/kgeorgiy/java/advanced/walk/samples/12
                        java/info/kgeorgiy/java/advanced/walk/samples/123
                        java/info/kgeorgiy/java/advanced/walk/samples/1234
                        java/info/kgeorgiy/java/advanced/walk/samples/1
                        java/info/kgeorgiy/java/advanced/walk/samples/binary
                        java/info/kgeorgiy/java/advanced/walk/samples/no-such-file
                    </pre><p>Выходной файл</p><pre>                        050c5d2e java/info/kgeorgiy/java/advanced/walk/samples/1
                        2076af58 java/info/kgeorgiy/java/advanced/walk/samples/12
                        72d607bb java/info/kgeorgiy/java/advanced/walk/samples/123
                        81ee2b55 java/info/kgeorgiy/java/advanced/walk/samples/1234
                        050c5d2e java/info/kgeorgiy/java/advanced/walk/samples/1
                        8e8881c5 java/info/kgeorgiy/java/advanced/walk/samples/binary
                        00000000 java/info/kgeorgiy/java/advanced/walk/samples/no-such-file
                    </pre></li></ol></li><li>
            Усложненная версия:
            <ol><li>
                    Разработайте класс <tt>RecursiveWalk</tt>,
                    осуществляющий подсчет хеш-сумм файлов в
                    директориях
                </li><li>
                    Входной файл содержит список файлов и директорий,
                    которые требуется обойти. Обход директорий осуществляется
                    рекурсивно.
                </li><li><p>Пример</p><p>Входной файл</p><pre>                        java/info/kgeorgiy/java/advanced/walk/samples/binary
                        java/info/kgeorgiy/java/advanced/walk/samples
                    </pre><p>Выходной файл</p><pre>                        8e8881c5 java/info/kgeorgiy/java/advanced/walk/samples/binary
                        050c5d2e java/info/kgeorgiy/java/advanced/walk/samples/1
                        2076af58 java/info/kgeorgiy/java/advanced/walk/samples/12
                        72d607bb java/info/kgeorgiy/java/advanced/walk/samples/123
                        81ee2b55 java/info/kgeorgiy/java/advanced/walk/samples/1234
                        8e8881c5 java/info/kgeorgiy/java/advanced/walk/samples/binary
                    </pre></li></ol></li><li>
            При выполнении задания следует обратить внимание на:
            <ul><li>
                    Дизайн и обработку исключений, диагностику ошибок.
                </li><li>
                    Программа должна корректно завершаться даже в случае ошибки.
                </li><li>
                    Корректная работа с вводом-выводом.
                </li><li>
                    Отсутствие утечки ресурсов.
                </li></ul></li><li>
            Требования к оформлению задания.
            <ul><li>
                    Проверяется исходный код задания.
                </li><li>
                    Весь код должен находиться в пакете
                    <tt>ru.ifmo.rain.фамилия.walk</tt>.
                </li></ul></li></ol><p><a href="https://www.kgeorgiy.info/git/geo/java-advanced-2019">Тесты к домашним заданиям</a></p><h3 id="homework-2">Домашнее задание 2. Множество на массиве</h3><ol><li>
            Разработайте класс <tt>ArraySet</tt>,
            реализующие неизменяемое упорядоченное множество.
            <ul><li>
                    Класс <tt>ArraySet</tt> должен реализовывать
                    интерфейс <tt>SortedSet</tt> (упрощенная версия)
                    или <tt>NavigableSet</tt> (усложненная версия).
                </li><li>
                    Все операции над множествами должны производиться с максимально возможной асимптотической эффективностью.
                </li></ul></li><li>
            При выполнении задания следует обратить внимание на:
            <ul><li>
                    Применение стандартных коллекций.
                </li><li>
                    Избавление от повторяющегося кода.
                </li></ul></li></ol><h3 id="homework-3">Домашнее задание 3. Студенты</h3><ol><li>
            Разработайте класс <tt>StudentDB</tt>,
            осуществляющий поиск по базе данных студентов.
            <ul><li>
                    Класс <tt>StudentDB</tt> должен реализовывать
                    интерфейс <tt>StudentQuery</tt> (простая версия)
                    или <tt>StudentGroupQuery</tt> (сложная версия).
                </li><li>
                    Каждый метод должен состоять из ровно одного оператора.
                    При этом длинные операторы надо разбивать на несколько строк.
                </li></ul></li><li>
            При выполнении задания следует обратить внимание на:
            <ul><li>
                    Применение лямбда-выражений и потоков.
                </li><li>
                    Избавление от повторяющегося кода.
                </li></ul></li></ol><h3 id="homework-4">Домашнее задание 4. Implementor</h3><ol><li>
            Реализуйте класс <tt>Implementor</tt>, который будет генерировать
            реализации классов и интерфейсов.
            <ul><li>
                    Аргументы командной строки: полное имя класса/интерфейса, для
                    которого требуется сгенерировать реализацию.
                </li><li>
                    В результате работы должен быть сгенерирован java-код класса с суффиксом
                    <tt>Impl</tt>, расширяющий (реализующий) указанный класс (интерфейс).
                </li><li>
                    Сгенерированный класс должен компилироваться без ошибок.
                </li><li>
                    Сгенерированный класс не должен быть абстрактным.
                </li><li>
                    Методы сгенерированного класса должны игнорировать свои аргументы и
                    возвращать значения по умолчанию.
                </li></ul></li><li>
            В задании выделяются три уровня сложности:
            <ul><li><em>Простой</em> — <tt>Implementor</tt> должен уметь реализовывать
                    только интерфейсы (но не классы). Поддержка generics не требуется.
                </li><li><em>Сложный</em> — <tt>Implementor</tt> должен уметь реализовывать
                    и классы и интерфейсы. Поддержка generics не требуется.
                </li><li><em>Бонусный</em> — <tt>Implementor</tt> должен уметь реализовывать
                    generic-классы и интерфейсы. Сгенерированный код должен иметь
                    корректные параметры типов и не порождать <tt>UncheckedWarning</tt>.
                </li></ul></li></ol><h3 id="homework-5">Домашнее задание 5. Jar Implementor</h3><ol><li>
            Создайте <tt>.jar</tt>-файл, содержащий скомпилированный
            <tt>Implementor</tt> и сопутствующие классы.
            <ul><li>
                    Созданный <tt>.jar</tt>-файл должен запускаться командой
                    <tt>java -jar</tt>.
                </li><li>
                    Запускаемый <tt>.jar</tt>-файл должен принимать те же аргументы командной
                    строки, что и класс <tt>Implementor</tt>.
                </li></ul></li><li>
            Модифицируйте <tt>Implemetor</tt> так, что бы
            при запуске с аргументами <tt>-jar имя-класса файл.jar</tt>
            он генерировал <tt>.jar</tt>-файл с реализацией
            соответствующего класса (интерфейса).
        </li><li>
            Для проверки, кроме исходного кода так же должны быть предъявлены:
            <ul><li>
                    скрипт для создания запускаемого <tt>.jar</tt>-файла, в том числе,
                    исходный код манифеста;
                </li><li>
                    запускаемый <tt>.jar</tt>-файл.
                </li></ul></li><li>
            Данное домашнее задание сдается только вместе с предыдущим.
            Предыдущее домашнее задание отдельно сдать будет нельзя.
        </li><li><b>Сложная версия</b>. Решение должно быть модуляризовано.
        </li></ol><h3 id="homework-6">Домашнее задание 6. Javadoc</h3><ol><li>
            Документируйте класс <tt>Implementor</tt> и сопутствующие классы с применением
            Javadoc.
            <ul><li>
                    Должны быть документированы все классы и все члены классов, в том числе
                    закрытые (<tt>private</tt>).
                </li><li>
                    Документация должна генерироваться без предупреждений.
                </li><li>
                    Сгенерированная документация должна содержать корректные ссылки на
                    классы стандартной библиотеки.
                </li></ul></li><li>
            Для проверки, кроме исходного кода так же должны быть предъявлены:
            <ul><li>
                    скрипт для генерации документации;
                </li><li>
                    сгенерированная документация.
                </li></ul></li><li>
            Данное домашнее задание сдается только вместе с предыдущим.
            Предыдущее домашнее задание отдельно сдать будет нельзя.
        </li></ol><h3 id="homework-7">Домашнее задание 7. Итеративный параллелизм</h3><ol><li>
            Реализуйте класс <tt>IterativeParallelism</tt>, 
            который будет обрабатывать списки в несколько потоков.
        </li><li>
            В <i>простом</i> варианте должны быть реализованы следующие методы:
            <ul><li><tt>minimum(threads, list, comparator)</tt> —
                    первый минимум;
                </li><li><tt>maximum(threads, list, comparator)</tt> —
                    первый максимум;
                </li><li><tt>all(threads, list, predicate)</tt> —
                    проверка, что все элементы списка удовлетворяют
                    <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html">предикату</a>;
                </li><li><tt>any(threads, list, predicate)</tt> —
                    проверка, что существует элемент списка, удовлетворяющий
                    <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html">предикату</a>.
                </li></ul></li><li>
            В <i>сложном</i> варианте должны быть дополнительно реализованы следующие методы:
            <ul><li><tt>filter(threads, list, predicate)</tt> —
                    вернуть список, содержащий элементы удовлетворяющие
                    <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html">предикату</a>;
                </li><li><tt>map(threads, list, function)</tt> —
                    вернуть список, содержащий результаты применения
                    <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html">функции</a>;
                </li><li><tt>join(threads, list)</tt> —
                    конкатенация строковых представлений
                    элементов списка.
                </li></ul></li><li>
            Во все функции передается параметр <tt>threads</tt> —
            сколько потоков надо использовать при вычислении.
            Вы можете рассчитывать, что число потоков не велико.
        </li><li>
            Не следует рассчитывать на то, что переданные компараторы,
            предикаты и функции работают быстро.
        </li><li>
            При выполнении задания нельзя использовать 
            <i>Concurrency Utilities</i>.
        </li><li>
            Рекомендуется подумать, какое отношение к
            заданию имеют <a href="https://en.wikipedia.org/wiki/Monoid">моноиды</a>.
        </li></ol><h3 id="homework-8">Домашнее задание 8. Параллельный запуск</h3><ol><li>
            Напишите класс <tt>ParallelMapperImpl</tt>, реализующий интерфейс 
            <tt>ParallelMapper</tt>.
<pre>public interface ParallelMapper extends AutoCloseable {
    &lt;T, R&gt; List&lt;R&gt; run(
        Function&lt;? super T, ? extends R&gt; f, 
        List&lt;? extends T&gt; args
    ) throws InterruptedException;

    @Override
    void close() throws InterruptedException;
}
</pre><ul><li>
                    Метод <tt>run</tt> должен параллельно вычислять 
                    функцию <tt>f</tt> на каждом из указанных аргументов 
                    (<tt>args</tt>).
                </li><li>
                    Метод <tt>close</tt> должен останавливать все рабочие потоки.
                </li><li>
                    Конструктор <tt>ParallelMapperImpl(int threads)</tt>
                    создает <tt>threads</tt> рабочих потоков, которые могут
                    быть использованы для распараллеливания.
                </li><li>
                    К одному <tt>ParallelMapperImpl</tt> могут одновременно обращаться
                    несколько клиентов.
                </li><li>
                    Задания на исполнение должны накапливаться в очереди и обрабатываться
                    в порядке поступления.
                </li><li>
                    В реализации не должно быть активных ожиданий.
                </li></ul></li><li>
            Модифицируйте касс <tt>IterativeParallelism</tt> так,
            чтобы он мог использовать <tt>ParallelMapper</tt>.
            <ul><li>
                    Добавьте конструктор <tt>IterativeParallelism(ParallelMapper)</tt></li><li>
                    Методы класса должны делить работу на <tt>threads</tt>
                    фрагментов и исполнять их при помощи <tt>ParallelMapper</tt>.
                </li><li>
                    Должна быть возможность одновременного запуска и работы
                    нескольких клиентов, использующих один <tt>ParallelMapper</tt>.
                </li><li>
                    При наличии <tt>ParallelMapper</tt> сам 
                    <tt>IterativeParallelism</tt> новые потоки создавать не должен.
                </li></ul></li></ol><h3 id="homework-9">Домашнее задание 9. Web Crawler</h3><ol><li>
            Напишите потокобезопасный класс <tt>WebCrawler</tt>, который
            будет рекурсивно обходить сайты.
            <ol><li>
                    Класс <tt>WebCrawler</tt> должен иметь конструктор
                    <pre>                        public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost)
                    </pre><ul><li><tt>downloader</tt> позволяет скачивать страницы и
                            извлекать из них ссылки;
                        </li><li><tt>downloaders</tt> — максимальное число
                            одновременно загружаемых страниц;
                        </li><li><tt>extractors</tt> — максимальное число страниц,
                            из которых извлекаются ссылки;
                        </li><li><tt>perHost</tt> — максимальное число страниц,
                            одновременно загружаемых c одного хоста.
                            Для опредения хоста следует использовать
                            метод <tt>getHost</tt> класса
                            <tt>URLUtils</tt> из тестов.
                        </li></ul></li><li>
                    Класс <tt>WebCrawler</tt> должен реализовывать интерфейс
                    <tt>Crawler</tt><pre>                        public interface Crawler extends AutoCloseable {
                            List&lt;String&gt; download(String url, int depth) throws IOException;

                            void close();
                        }
                    </pre><ul><li>
                            Метод <tt>download</tt> должен рекурсивно обходить страницы,
                            начиная с указанного URL на указанную глубину и
                            возвращать список загруженных страниц и файлов.

                            Например, если глубина равна 1, то должна быть
                            загружена только указанная страница. Если глубина равна
                            2, то указанная страница и те страницы и файлы, на которые
                            она ссылается и так далее.

                            Этот метод может вызываться параллельно в нескольких потоках.
                        </li><li>
                            Загрузка и обработка страниц (извлечение ссылок)
                            должна выполняться максимально параллельно,
                            с учетом ограничений на число одновременно
                            загружаемых страниц (в том числе с одного хоста)
                            и страниц, с которых загружаются ссылки.
                        </li><li>
                            Для распараллеливания разрешается создать
                            до <tt>downloaders + extractors</tt>
                            вспомогательных потоков.
                        </li><li>
                            Загружать и/или извлекать ссылки из одной
                            и той же страницы в рамках одного обхода
                            (<tt>download</tt>) запрещается.
                        </li><li>
                            Метод <tt>close</tt> должен завершать все
                            вспомогательные потоки.
                        </li></ul></li><li>
                    Для загрузки страниц должен применяться <tt>Downloader</tt>,
                    передаваемый первым аргументом конструктора.
                    <pre>                        public interface Downloader {
                            public Document download(final String url) throws IOException;
                        }
                    </pre><ul><li>
                            Метод <tt>download</tt> загружает документ по его адресу
                            (<a href="http://tools.ietf.org/html/rfc3986">URL</a>).
                        </li><li>
                            Документ позволяет получить ссылки по загруженной странице:
                <pre>                    public interface Document {
                        List&lt;String&gt; extractLinks() throws IOException;
                    }
                </pre>
                            Ссылки, возвращаемые документом являются абсолютными
                            и имеют схему <tt>http</tt> или <tt>https</tt>.
                        </li></ul></li><li>
                    Должен быть реализован метод <tt>main</tt>,
                    позволяющий запустить обход из командной строки
                    <ul><li>
                            Командная строка
                <pre>                    WebCrawler url [depth [downloads [extractors [perHost]]]]
                </pre></li><li>
                            Для загрузки страниц требуется использовать реализацию
                            <tt>CachingDownloader</tt> из тестов.
                        </li></ul></li></ol></li><li>
            Версии задания
            <ol><li><em>Простая</em> — можно не учитывать ограничения
                    на число одновременных закачек с одного хоста
                    <tt>(perHost &gt;= downloaders)</tt>.
                </li><li><em>Полная</em> — требуется учитывать все ограничения.
                </li><li><em>Бонусная</em> — сделать параллельный обод в ширину.
                </li></ol></li></ol><h3 id="homework-10">Домашнее задание 10. HelloUDP</h3><ol><li>
            Реализуйте клиент и сервер, взаимодействующие по UDP.
        </li><li>
            Класс <tt>HelloUDPClient</tt> должен отправлять запросы
            на сервер, принимать результаты и выводить их на консоль.
            <ul><li>
                    Аргументы командной строки:
                    <ol><li>имя или ip-адрес компьютера, на котором запущен сервер;</li><li>номер порта, на который отсылать запросы;</li><li>префикс запросов (строка);</li><li>число параллельных потоков запросов;</li><li>число запросов в каждом потоке.</li></ol></li><li>
                    Запросы должны одновременно отсылаться в указанном числе потоков.
                    Каждый поток должен ожидать обработки своего запроса и выводить
                    сам запрос и результат его обработки на консоль.
                    Если запрос не был обработан, требуется послать его заного.
                </li><li>
                    Запросы должны формироваться по схеме
                    <tt>&lt;префикс запросов&gt;&lt;номер потока&gt;_&lt;номер запроса в потоке&gt;</tt>.
                </li></ul></li><li>
            Класс <tt>HelloUDPServer</tt> должен принимать задания, отсылаемые
            классом <tt>HelloUDPClient</tt> и отвечать на них.
            <ul><li>
                    Аргументы командной строки:
                    <ol><li>номер порта, по которому будут приниматься запросы;</li><li>число рабочих потоков, которые будут обрабатывать запросы.</li></ol></li><li>
                    Ответом на запрос должно быть <tt>Hello, &lt;текст запроса&gt;</tt>.
                </li><li>
                    Если сервер не успевает обрабатывать запросы, прием запросов может
                    быть временно приостановлен.
                </li></ul></li><li><em>Бонусный вариант</em>. Реализация должна быть полностью неблокирующей.
            <ul><li>
                	Клиент не должен создавать потоков.
                </li><li>
            		В реализации не должно быть активных ожиданий,
                    в том числе через <code>Selector</code>.
                </li></ul></li></ol><h3 id="homework-11">Домашнее задание 11. Физические лица</h3><ol><li>
            Добавьте к банковскому приложению возможность работы с физическими
            лицами.
            <ol><li>
                    У физического лица (<tt>Person</tt>) можно запросить имя, фамилию и номер паспорта.
                </li><li>
                    Локальные физические лица (<tt>LocalPerson</tt>) должны передаваться при помощи
                    механизма сериализации.
                </li><li>
                    Удаленные физические лица (<tt>RemotePerson</tt>) должны передаваться при помощи
                    удаленных объектов.
                </li><li>
                    Должна быть возможность поиска физического лица по
                    номеру паспорта, с выбором типа возвращаемого лица.
                </li><li>
                    Должна быть возможность создания записи о физическом лице по его данным.
                </li><li>
                    У физического лица может быть несколько счетов, к которым должен
                    предоставляться доступ.
                </li><li>
                    Счету физического лица с идентификатором <var>subId</var>
                    должен соответствовать банковский счет с <var>id</var>
                    вида <var>passport</var>:<var>subId</var>.
                </li><li>
                    Изменения, производимые со счетом в банке
                    (создание и изменение баланса), должны быть видны всем
                    соответствующим <var>RemotePerson</var>, и только тем
                    <var>LocalPerson</var>, которые были созданы после этого изменения.
                </li><li>
                    Изменения в счетах, производимые через <var>RemotePerson</var>
                    должны сразу применяться глобально, а призводимые
                    через <var>LocalPerson</var> – только локально
                    для этого конкретного <var>LocalPerson</var>.
                </li></ol></li><li>
            Напишите тесты, проверяющее вышеуказанное поведение.
        </li><li>
            Реализуйте приложение, демонстрирующее работу с физическим лицами.
            <ol><li>
                    Аргументы командной строки: имя, фамилия, номер паспорта физического
                    лица, номер счета, изменение суммы счета.
                </li><li>
                    Если информация об указанном физическом лице отсутствует, то оно должно
                    быть добавлено. В противном случае – должны быть проверены его данные.
                </li><li>
                    Если у физического лица отсутствует счет с указанным номером, то
                    он создается с нулевым балансом.
                </li><li>
                    После обновления суммы счета, новый баланс должен выводиться на консоль.
                </li></ol></li></ol><h3 id="homework-12">Домашнее задание 12. Статистика текста</h3><ol><li>
            Создайте приложение <code>TextStatistics</code>,
            анализирующее тексты на различных языках.
            <ol><li>
                    Аргументы командной строки:
                    <pre>TextStatistics &lt;локаль текста&gt; &lt;локаль вывода&gt; &lt;файл с текстом&gt; &lt;файл отчета&gt;</pre></li><li>
                    Поддерживаемые локали текста: все локали, имеющиеся в системе.
                </li><li>
                    Поддерживаемые локали вывода: русская и английская.
                </li><li>
                    Файлы имеют кодировку UTF-8.
                </li><li>
                    Подсчет статистики должен вестись по следующим категориям:
                    <ul><li>предложения</li><li>строки</li><li>слова</li><li>числа</li><li>деньги</li><li>даты</li></ul></li><li>
                    Для каждой категории должна собираться следующая статистика:
                    <ul><li>число вхождений</li><li>число различных значений</li><li>минимальное значение</li><li>максимальное значение</li><li>минимальная длина</li><li>максимальная длина</li><li>среднее значение/длина</li></ul></li><li>
                    Отчет должен выводиться в формате HTML.
                </li><li>
                    Пример отчета:
                    <div style="background-color: #ddd"><h3 id="N66344">Анализируемый файл: input.txt</h3><h4 id="N66346">Сводная статистика</h4><p>Число предложений: 38</p><p>Число строк: 41</p><p>…</p><h4 id="N66354">Статистика по словам</h4><p>Число слов: 153 (95 уникальных)</p><p>Минимальное слово: HTML</p><p>Максимальное слово: языках</p><p>Минимальная длина слова: 1 (и)</p><p>Максимальная длина слова: 14 (Поддерживаемые) </p><p>Средняя длина слова: 10</p><h4 id="N66368">Статистика по …</h4></div></li></ol></li><li>
            При выполнении задания следует обратить внимание на:
            <ol><li>Декомпозицию сообщений для локализации</li><li>Согласование предложений</li></ol></li></ol></td>
            
# Тесты к курсу «Технологии Java»

[Условия домашних заданий](http://www.kgeorgiy.info/courses/java-advanced/homeworks.html)


## Домашнее задание 10. HelloUDP

Тестирование

 * простой вариант:
    * клиент:
        ```info.kgeorgiy.java.advanced.hello client <полное имя класса>```
    * сервер:
        ```info.kgeorgiy.java.advanced.hello server <полное имя класса>```
 * сложный вариант:
    * клиент:
        ```info.kgeorgiy.java.advanced.hello client-i18n <полное имя класса>```
    * сервер:
        ```info.kgeorgiy.java.advanced.hello server-i18n <полное имя класса>```

Исходный код тестов:

* [Клиент](modules/info.kgeorgiy.java.advanced.hello/info/kgeorgiy/java/advanced/hello/HelloClientTest.java)
* [Сервер](modules/info.kgeorgiy.java.advanced.hello/info/kgeorgiy/java/advanced/hello/HelloServerTest.java)


## Домашнее задание 9. Web Crawler

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.crawler easy <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.crawler hard <полное имя класса>```

Исходный код тестов:

* [интерфейсы и вспомогательные классы](modules/info.kgeorgiy.java.advanced.crawler/info/kgeorgiy/java/advanced/crawler/)
* [простой вариант](modules/info.kgeorgiy.java.advanced.crawler/info/kgeorgiy/java/advanced/crawler/CrawlerEasyTest.java)
* [сложный вариант](modules/info.kgeorgiy.java.advanced.crawler/info/kgeorgiy/java/advanced/crawler/CrawlerHardTest.java)


## Домашнее задание 8. Параллельный запуск

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.mapper scalar <ParallelMapperImpl>,<IterativeParallelism>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.mapper list <ParallelMapperImpl>,<IterativeParallelism>```

Внимание! Между полными именами классов `ParallelMapperImpl` и `IterativeParallelism`
должна быть запятая и не должно быть пробелов.

Исходный код тестов:

* [простой вариант](modules/info.kgeorgiy.java.advanced.mapper/info/kgeorgiy/java/advanced/mapper/ScalarMapperTest.java)
* [сложный вариант](modules/info.kgeorgiy.java.advanced.mapper/info/kgeorgiy/java/advanced/mapper/ListMapperTest.java)


## Домашнее задание 7. Итеративный параллелизм

Тестирование

 * простой вариант:
   ```info.kgeorgiy.java.advanced.concurrent scalar <полное имя класса>```

   Класс должен реализовывать интерфейс
   [ScalarIP](modules/info.kgeorgiy.java.advanced.concurrent/info/kgeorgiy/java/advanced/concurrent/ScalarIP.java).

 * сложный вариант:
   ```info.kgeorgiy.java.advanced.concurrent list <полное имя класса>```

   Класс должен реализовывать интерфейс
   [ListIP](modules/info.kgeorgiy.java.advanced.concurrent/info/kgeorgiy/java/advanced/concurrent/ListIP.java).

Исходный код тестов:

* [простой вариант](modules/info.kgeorgiy.java.advanced.concurrent/info/kgeorgiy/java/advanced/concurrent/ScalarIPTest.java)
* [сложный вариант](modules/info.kgeorgiy.java.advanced.concurrent/info/kgeorgiy/java/advanced/concurrent/ListIPTest.java)


## Домашнее задание 5. JarImplementor

Класс должен реализовывать интерфейс
[JarImpler](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/JarImpler.java).

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.implementor jar-interface <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.implementor jar-class <полное имя класса>```

Исходный код тестов:

* [простой вариант](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/InterfaceJarImplementorTest.java)
* [сложный вариант](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/ClassJarImplementorTest.java)


## Домашнее задание 4. Implementor

Класс должен реализовывать интерфейс
[Impler](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/Impler.java).

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.implementor interface <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.implementor class <полное имя класса>```

Исходный код тестов:

* [простой вариант](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/InterfaceImplementorTest.java)
* [сложный вариант](modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/ClassImplementorTest.java)


## Домашнее задание 3. Студенты

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.student StudentQuery <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.student StudentGroupQuery <полное имя класса>```

Исходный код

 * простой вариант:
    [интерфейс](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/StudentQuery.java),
    [тесты](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/FullStudentQueryTest.java)
 * сложный вариант:
    [интерфейс](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/StudentGroupQuery.java),
    [тесты](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/FullStudentGroupQueryTest.java)
 * продвинутый вариант:
    [интерфейс](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/AdvancedStudentGroupQuery.java),
    [тесты](modules/info.kgeorgiy.java.advanced.student/info/kgeorgiy/java/advanced/student/AdvancedStudentGroupQueryTest.java)


## Домашнее задание 2. ArraySortedSet

Тестирование

 * простой вариант:
    ```info.kgeorgiy.java.advanced.arrayset SortedSet <полное имя класса>```
 * сложный вариант:
    ```info.kgeorgiy.java.advanced.arrayset NavigableSet <полное имя класса>```

Исходный код тестов:

 * [простой вариант](modules/info.kgeorgiy.java.advanced.arrayset/info/kgeorgiy/java/advanced/arrayset/SortedSetTest.java)
 * [сложный вариант](modules/info.kgeorgiy.java.advanced.arrayset/info/kgeorgiy/java/advanced/arrayset/NavigableSetTest.java)


## Домашнее задание 1. Обход файлов

Для того, чтобы протестировать программу:

 * Скачайте
    * тесты
        * [info.kgeorgiy.java.advanced.base.jar](artifacts/info.kgeorgiy.java.advanced.base.jar)
        * [info.kgeorgiy.java.advanced.walk.jar](artifacts/info.kgeorgiy.java.advanced.walk.jar)
    * и библиотеки к ним:
        * [junit-4.11.jar](lib/junit-4.11.jar)
        * [hamcrest-core-1.3.jar](lib/hamcrest-core-1.3.jar)
 * Откомпилируйте решение домашнего задания
 * Протестируйте домашнее задание
    * Текущая директория должна:
       * содержать все скачанные `.jar` файлы;
       * содержать скомпилированное решение;
       * __не__ содержать скомпилированные самостоятельно тесты.
    * простой вариант:
        ```java -cp . -p . -m info.kgeorgiy.java.advanced.walk Walk <полное имя класса>```
    * сложный вариант:
        ```java -cp . -p . -m info.kgeorgiy.java.advanced.walk RecursiveWalk <полное имя класса>```

Исходный код тестов:

 * [простой вариант](modules/info.kgeorgiy.java.advanced.walk/info/kgeorgiy/java/advanced/walk/WalkTest.java)
 * [сложный вариант](modules/info.kgeorgiy.java.advanced.walk/info/kgeorgiy/java/advanced/walk/RecursiveWalkTest.java)
