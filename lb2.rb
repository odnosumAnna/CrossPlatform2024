require 'benchmark' # Імпортуємо модуль Benchmark для вимірювання продуктивності коду.

# Клас Report: відповідає за створення базової структури HTML-звіту.
class Report
  attr_reader :content # Забезпечує доступ до змінної @content через геттер.

  # Ініціалізатор класу Report. Приймає заголовок звіту і створює початкову HTML-структуру.
  def initialize(title)
    # Формує базову частину HTML-документа: <html>, <head> із заголовком, <body>, <h1>.
    @content = "<html>\n<head>\n<title>#{title}</title>\n</head>\n<body>\n<h1>#{title}</h1>\n"
  end

  # Метод для додавання текстового абзацу до звіту.
  def add_paragraph(text)
    # Додає тег <p> із текстом, переданим у метод.
    @content += "<p>#{text}</p>\n"
  end

  # Метод для додавання таблиці до звіту.
  def add_table(data)
    # Починає таблицю з тегу <table> із рамкою.
    @content += "<table border='1'>\n"
    # Проходить по кожному рядку даних.
    data.each do |row|
      # Додає тег <tr> для створення нового рядка.
      @content += "  <tr>\n"
      # Проходить по кожній комірці рядка і додає тег <td> із вмістом комірки.
      row.each { |cell| @content += "    <td>#{cell}</td>\n" }
      # Закриває тег рядка.
      @content += "  </tr>\n"
    end
    # Закриває таблицю.
    @content += "</table>\n"
  end

  # Метод для завершення формування HTML-документа.
  def to_html
    # Додає закриваючі теги </body> і </html> до вмісту.
    @content + "</body>\n</html>\n"
  end
end

# Клас ReportDSL: реалізує DSL для створення звітів.
class ReportDSL
  # Ініціалізатор класу. Приймає заголовок звіту та блок DSL-команд.
  def initialize(title, &block)
    # Створює об'єкт класу Report для управління звітом.
    @report = Report.new(title)
    # Виконує переданий блок у контексті екземпляра класу, якщо блок наданий.
    instance_eval(&block) if block_given?
  end

  # Метод DSL для додавання абзацу до звіту.
  def paragraph(text)
    # Викликає метод add_paragraph класу Report.
    @report.add_paragraph(text)
  end

  # Метод DSL для додавання таблиці до звіту.
  def table(data)
    # Викликає метод add_table класу Report.
    @report.add_table(data)
  end

  # Метод для отримання готового HTML-звіту.
  def to_html
    # Викликає метод to_html класу Report для завершення звіту.
    @report.to_html
  end
end


