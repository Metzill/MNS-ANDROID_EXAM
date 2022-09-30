package com.example.mns_android_exam;

import org.json.JSONException;
import org.json.JSONObject;

class Quote {

   protected String anime;
   protected String character;
   protected String quote;

   public Quote(String anime, String character, String quote) {
      this.anime = anime;
      this.character = character;
      this.quote = quote;
   }

   public Quote(JSONObject json) throws JSONException {
      this.anime = json.getString("anime");
      this.character = json.getString("character");
      this.quote = json.getString("quote");
   }

   public String getAnime() {
      return anime;
   }

   public void setAnime(String anime) {
      this.anime = anime;
   }

   public String getCharacter() {
      return character;
   }

   public void setCharacter(String character) {
      this.character = character;
   }

   public String getQuote() {
      return quote;
   }

   public void setQuote(String quote) {
      this.quote = quote;
   }
}
