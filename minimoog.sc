(
SynthDef(\minimoog,{
	arg out, pan, freq=80, osc1wf=0, osc2wf=0, osc3wf=0, osc1pw=0.5, osc2pw=0.5, osc3pw=0.5, osc1range=0, osc2range=0, osc3range=0, osc2tune=0, osc3tune=0, osc1vol=0.5, osc2vol=0.5, osc3vol=0.5, osc1on=1, osc2on=0, osc3on=0, noisetype=0, noisevol=0.5, noiseon=0, glide=0, cutoff=20000, res=0, filtatk=0.01, filtdec=0.3, filtsus=0.5;

	var sig;

	//selecting octaves
	var osc1oct = (12 * osc1range).midicps;
	var osc2oct = (12 * osc2range).midicps;
	var osc3oct = (12 * osc3range).midicps;

	//selecting waveforms, octave, and detune
	var osc1 = SelectX.ar(osc1wf,
		[SinOsc.ar(freq + osc1oct),
			Saw.ar(freq + osc1oct),
			Pulse.ar(freq + osc1oct, osc1pw)]);

	var osc2 = SelectX.ar(osc2wf,
		[SinOsc.ar(freq + osc2oct + osc2tune.midicps),
			Saw.ar(freq + osc2oct + osc2tune.midicps),
			Pulse.ar(freq + osc2oct + osc2tune.midicps, osc2pw)]);

	var osc3 = SelectX.ar(osc3wf,
		[SinOsc.ar(freq + osc3oct + osc3tune.midicps),
			Saw.ar(freq + osc3oct + osc3tune.midicps),
			Pulse.ar(freq + osc3oct + osc3tune.midicps, osc3pw)]);

	var noise = SelectX.ar(noisetype,
		[WhiteNoise.ar,
			PinkNoise.ar]);

	//on switches and volume
	osc1 = osc1 * osc1on * osc1vol;
	osc2 = osc2 * osc2on * osc2vol;
	osc3 = osc3 * osc3on * osc3vol;
	noise = noise * noiseon * noisevol;

	 sig = osc1 + osc2 + osc3 + noise;

	//filter envelope
	cutoff = cutoff * EnvGen.kr(Env.adsr(filtatk, filtdec, filtsus, filtdec));

	//filter and glide
	sig = MoogLadder.ar(sig, cutoff, res).lag(glide);

	//out
	Out.ar(out, DirtPan.ar(sig, ~dirt.numChannels, pan));

}).add
)

//there are 3 oscilatrs, each one has a selectable waveform and a selectable octave
//oscilators 2 and 3 also have a fine tune knob that can go 7 semitones in either direction
//each oscilator also has a volume knob and an on/off switch
//there is also a noise oscilator with selectable pink or white noise, a volume knob, and an on/off switch

//There is a lpf with a cutoff frequency, resonance, and an envelope
// the envelope has adsd and an "amount of contour" which how much effect the envelope has
//the curves on the envelope are not linear and not adjustable
//the amplitude envelope will be handled by tidalcycles

//there is a glide knob

s.plotTree
