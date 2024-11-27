# frozen_string_literal: true
require 'benchmark' # Підключення модуля для вимірювання часу виконання

# Клас для пошуку найбільшої загальної підпослідовності (LCS)
class LongestCommonSubsequence
  # Метод для пошуку LCS між двома рядками
  def self.find_lcs(str1, str2)
    # Перевірка на те, що обидва параметри є рядками
    raise ArgumentError, 'Параметри повинні бути рядками' unless str1.is_a?(String) && str2.is_a?(String)

    m = str1.length # Довжина першого рядка
    n = str2.length # Довжина другого рядка

    # Ініціалізація матриці для динамічного програмування розміром (m+1)x(n+1)
    lcs_matrix = Array.new(m + 1) { Array.new(n + 1, 0) }

    # Заповнення матриці шляхом ітерації по символах обох рядків
    (1..m).each do |i|
      (1..n).each do |j|
        if str1[i - 1] == str2[j - 1] # Якщо символи збігаються
          lcs_matrix[i][j] = lcs_matrix[i - 1][j - 1] + 1 # Додаємо 1 до діагонального значення
        else
          # Беремо максимальне значення з верхньої чи лівої клітинки
          lcs_matrix[i][j] = [lcs_matrix[i - 1][j], lcs_matrix[i][j - 1]].max
        end
      end
    end

    # Відновлення LCS шляхом проходження матриці з кінця
    lcs_string = "" # Рядок для збереження результату
    i, j = m, n # Починаємо з правого нижнього кута матриці

    while i > 0 && j > 0
      if str1[i - 1] == str2[j - 1] # Якщо символи збігаються
        lcs_string.prepend(str1[i - 1]) # Додаємо символ до результату
        i -= 1 # Перехід вгору-ліворуч
        j -= 1
      elsif lcs_matrix[i - 1][j] >= lcs_matrix[i][j - 1]
        i -= 1 # Перехід вгору
      else
        j -= 1 # Перехід вліво
      end
    end

    return lcs_string # Повертаємо результат
  end

  # Метод для пошуку LCS серед кількох рядків
  def self.find_multi_lcs(*strings)
    # Перевірка, чи всі параметри є рядками
    strings.each do |str|
      raise ArgumentError, 'Усі параметри повинні бути рядками' unless str.is_a?(String)
    end

    return '' if strings.empty? # Повертаємо порожній рядок, якщо вхідний список порожній

    lcs_result = strings[0] # Початковий LCS дорівнює першому рядку
    strings[1..].each do |str| # Для кожного наступного рядка
      lcs_result = find_lcs(lcs_result, str) # Знаходимо LCS між поточним результатом та рядком
    end

    lcs_result # Повертаємо результат
  end
end
