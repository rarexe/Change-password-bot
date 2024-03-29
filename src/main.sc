require: slotfilling/slotFilling.sc
  module = sys.zb-common
require: patterns.sc
theme: /
    
    state: Hello
        q!: $regex</start>
        a: Здравствуйте!
        
    state: ChangePassword
        q!: * {[$change|$restore|$find|$findOut|$get|$install|сброс*|ввод*|ввест*|введ*|запрос*|запрош*|созда*|операц*|оповещен*|*смотр*|приход] * $password} *
        a: Сейчас расскажу порядок действий.
            Выберите, что именно планируете сделать:
            1. Поменять пароль для входа в приложение.
            2. Поменять PIN-код от карты.
            Пожалуйста, отправьте цифру, соответствующую вашему выбору.
        timeout: /Goodbye || interval = "30 minutes"
        
        state: ChangePasswordApp
            q: (1|один|перв*|$app) || fromState = "/ChangePassword" 
            q!: * {($change|$restore|$find|$findOut|$get|$install|сброс*|ввод*|ввест*|введ*|запрос*|запрош*|созда*|операц*|*смотр*|приход*|перевод*) * $password * ($app|$lkk|$site|регистрац*|оповещен*|телефон*|настройк*)} * 
            q!: * {[$change|$restore|$find|$findOut|$get|$install|сброс*|ввод*|ввест*|введ*|запрос*|запрош*|созда*|операц*|оповещен*|*смотр*|приход] * $password * {$enter * ($app|$lkk|$site|банк*|телефон*)}} * 
            a: Смена пароля от приложения возможна несколькими способами:
                1. на экране "Профиль" выберите "Изменить код входа в приложение".
                2. введите SMS-код.
                3. придумайте новый код для входа в приложение и повторите его. 
            timeout: /ChangePassword/ChangePasswordApp/ChangePasswordApp_ver2 || interval = "2 seconds"
                
            state: ChangePasswordApp_ver2
                a: Либо нажмите на кнопку "Выйти" на странице ввода пароля для входа в приложение. 
                    После чего нужно будет заново пройти регистрацию: 
                    1. ввести полный номер карты (если оформляли ранее, иначе номер телефона и дату рождения), 
                    2. указать код из смс-код,
                    3. придумать новый пароль для входа.
                timeout: /Goodbye || interval = "2 seconds"
            
        
        state: ChangePasswordCard
            q: (2|два|втор*|карт*) || fromState = "/ChangePassword" 
            q!: * {[$change|$restore|$find|$findOut|$get|$install|как|где*|сброс*|ввод*|ввест*|введ*|запрос*|запрош*|созда*|сдела*|операц*|*смотр*|приход*|*помн*|забы*|забил*|*скаж*|запис*|запиш*|не верн*|неверн*] * ($password|можнопосмотретьпинкод) * (карт*|кпрт*|банкомат*)} *
            a: Это можно сделать в приложении:
                1. На экране "Мои деньги" в разделе "Карты" нажмите на нужную.
                2. Выберите вкладку "Настройки".
                3. Нажмите "Сменить пин-код".
                4. И введите комбинацию, удобную вам.
                5. Повторите ее.
            a: И все готово! Пин-код установлен, можно пользоваться. 
            go!: /Goodbye
    
    state: NoMatch
        event!: noMatch
        go!: /ChangePassword
            
    state: Goodbye
        a: Приятно было пообщаться. Всегда готов помочь вам снова!

