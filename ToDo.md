2
Написать все оставшиеся test для Servis слоя, для createUser (естественно, используя Mockito)

3 чуть сложнее, для желающих .....
Вспомтнаем TDD: Добавить в интерфейс UserService иетод updateUser(Long userID, String newName, String newEmail). Именно в сервис! Реализовать тест для этого метода. Обратите внимание, сам метод в сервис слое пока реализовывать не надо (вспоминаем TDD), только сигеатура. Слой репозитория тоже не меняем, используем Mockito.

Подсказка: видимо, updateUser должн быть похожим на createNewUser (т.е. не позволять, например, менять email с правильного на неправельный, или ввести пустое имя).