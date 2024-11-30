# Клас ReportDSL для генерації звітів
class ReportDSL
  # Атрибути для зберігання назви та контенту звіту
  attr_accessor :title, :content

  # Ініціалізація об'єкта з початковими значеннями
  def initialize
    @title = "Untitled Report"  # Початкова назва звіту
    @content = []  # Порожній масив для зберігання контенту звіту (заголовки, абзаци, таблиці тощо)
  end

  # Метод для встановлення назви звіту
  def set_title(title)
    @title = title
  end

  # Метод для додавання заголовку в звіт
  def add_heading(text, level: 1)
    @content << "<h#{level}>#{text}</h#{level}>"  # Додає HTML тег для заголовку з вказаним рівнем
  end

  # Метод для додавання абзацу в звіт
  def add_paragraph(text)
    @content << "<p>#{text}</p>"  # Додає HTML тег для абзацу
  end

  # Метод для додавання таблиці в звіт
  def add_table(headers, rows)
    table = "<table border='1'>"  # Початок таблиці з обрамленням
    table << "<tr>" + headers.map { |header| "<th>#{header}</th>" }.join + "</tr>"  # Створює рядок заголовків таблиці
    rows.each do |row|  # Для кожного ряду даних у таблиці
      table << "<tr>" + row.map { |cell| "<td>#{cell}</td>" }.join + "</tr>"  # Створює рядки таблиці з даними
    end
    table << "</table>"  # Закриває тег таблиці
    @content << table  # Додає таблицю до контенту звіту
  end

  # Метод для генерації HTML-коду звіту
  def generate_html
    <<~HTML
      <!DOCTYPE html>
      <html>
      <head>
        <title>#{@title}</title>  # Вставляє назву звіту в тег title
      </head>
      <body>
        <h1>#{@title}</h1>  # Виводить назву звіту на сторінці
        #{@content.join("\n")}  # Додає всі елементи контенту, розділені новими рядками
      </body>
      </html>
    HTML
  end

  # Метод для генерації текстового формату звіту
  def generate_txt
    txt = "#{@title}\n\n"  # Додає назву звіту
    @content.each do |item|  # Для кожного елементу контенту
      txt << "#{item}\n\n"  # Додає елемент і порожній рядок після нього
    end
    txt  # Повертає весь текстовий звіт
  end

  # Метод для збереження звіту у файл з відповідним розширенням
  def save_to_file(file_name)
    case File.extname(file_name).downcase  # Визначає тип файлу за розширенням
    when ".html"
      File.write(file_name, generate_html)  # Зберігає звіт у HTML-форматі
      puts "Звіт збережено у файл: #{file_name}"
    when ".txt"
      File.write(file_name, generate_txt)  # Зберігає звіт у текстовому форматі
      puts "Звіт збережено у текстовий файл: #{file_name}"
    else
      puts "Невідомий формат файлу. Будь ласка, введіть .html або .txt."  # Якщо розширення неправильне
    end
  end
end

# Метод для початку діалогу з користувачем і генерації звіту
def start_dialog
  report = ReportDSL.new  # Створюємо новий об'єкт для звіту

  # Інтерфейс користувача для введення даних
  puts "Ласкаво просимо до генератора звітів!"
  puts "Введіть назву звіту:"
  report.set_title(gets.chomp)  # Встановлюємо назву звіту

  loop do  # Цикл для взаємодії з користувачем
    puts "\nЩо ви хочете додати?"
    puts "1. Заголовок"
    puts "2. Абзац"
    puts "3. Таблицю"
    puts "4. Завершити та зберегти звіт"
    print "Ваш вибір: "
    choice = gets.chomp.to_i  # Читання вибору користувача

    # Виконання дій залежно від вибору
    case choice
    when 1
      print "Введіть текст заголовку: "
      text = gets.chomp  # Вводить текст заголовку
      print "Рівень заголовку (1-6): "
      level = gets.chomp.to_i  # Вводить рівень заголовку
      if level < 1 || level > 6  # Перевірка на правильність рівня заголовку
        puts "Невірний рівень заголовку, використано рівень 1."
        level = 1  # Якщо рівень не в межах, використовуємо рівень 1
      end
      report.add_heading(text, level: level)  # Додає заголовок до звіту
    when 2
      print "Введіть текст абзацу: "
      text = gets.chomp  # Вводить текст абзацу
      report.add_paragraph(text)  # Додає абзац до звіту
    when 3
      print "Введіть заголовки таблиці через кому: "
      headers = gets.chomp.split(",").map(&:strip)  # Вводить заголовки таблиці
      rows = []  # Масив для рядків таблиці
      loop do  # Цикл для введення рядків таблиці
        print "Введіть рядок таблиці (значення через кому) або залиште пустим для завершення: "
        row = gets.chomp  # Вводить рядок таблиці
        break if row.empty?  # Завершує введення, якщо рядок порожній
        rows << row.split(",").map(&:strip)  # Додає рядок до таблиці
      end
      report.add_table(headers, rows)  # Додає таблицю до звіту
    when 4
      print "Введіть ім'я файлу для збереження звіту (з розширенням .html або .txt): "
      file_name = gets.chomp  # Вводить ім'я файлу
      if !file_name.end_with?(".html", ".txt")  # Перевірка на правильність розширення файлу
        puts "Невірне розширення файлу. Будь ласка, введіть .html або .txt."
        next  # Пропускає наступні дії, якщо розширення неправильне
      end
      report.save_to_file(file_name)  # Зберігає звіт у файл
      break  # Завершує цикл
    else
      puts "Неправильний вибір. Спробуйте ще раз."  # Якщо вибір неправильний
    end
  end
end

# Запуск програми
start_dialog  # Починає діалог і створення звіту
