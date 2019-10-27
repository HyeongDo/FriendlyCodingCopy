package com.mashup.friendlycoding.model

//DPS : Damage Per Second 로 공격력을 의미
class Princess (val DPS : Int){
    val spell = "Catch(e : Exception)!!!"

    fun attacked() {
        //TODO : 패배 메시지 띄우고 PlayActivity 종료, MainActivity 초기화
    }

    fun attack(monster: Monster) {
        //TODO : 공주의 샤라랑 한 공격을 보여줘야 한다
        monster.attacked(DPS)
    }

    fun castSpell(spell : String) {
        //TODO : 공주가 주문을 외우고 승리 메시지 띄운 후 PlayActivity 종료
    }
}