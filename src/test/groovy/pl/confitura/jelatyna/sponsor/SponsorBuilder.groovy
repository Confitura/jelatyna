package pl.confitura.jelatyna.sponsor

import groovy.json.JsonBuilder

class SponsorBuilder {
    def Sponsor sponsor = new Sponsor();

    def id(id){
        sponsor.id = id;
    }
    def name(name) {
        sponsor.name = name;
    }

    def type(type){
        sponsor.type = type;
    }

    def info(info){
        sponsor.info = info;
    }
    def url(url){
        sponsor.url = url;
    }

    public static Sponsor aSponsor(block) {
        def builder = new SponsorBuilder();
        builder.with block;
        return builder.sponsor;
    }

    public static JsonBuilder json(block) {
        def sponsor = aSponsor(block)
        return new JsonBuilder(sponsor);
    }
}
